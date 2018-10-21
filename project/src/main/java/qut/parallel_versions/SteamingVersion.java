package qut.parallel_versions;

import edu.au.jacobi.pattern.Match;
import edu.au.jacobi.pattern.Series;
import jaligner.BLOSUM62;
import jaligner.Sequence;
import jaligner.SmithWatermanGotoh;
import jaligner.matrix.Matrix;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import qut.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The final stream version.
 *
 * @author Jordi Smit on 11-10-2018.
 */
@SuppressWarnings("ALL")
public class SteamingVersion implements ISequential {
    private static final Matrix BLOSUM_62 = BLOSUM62.Load();
    @Getter
    private Map<String, Sigma70Consensus> consensus = new HashMap<>();
    private static ThreadLocal<Series> sigma70_pattern = ThreadLocal.withInitial(() ->Sigma70Definition.getSeriesAll_Unanchored(0.7));
    private byte[] complement = new byte['z'];

    public SteamingVersion() {
        complement['C'] = 'G';
        complement['c'] = 'g';
        complement['G'] = 'C';
        complement['g'] = 'c';
        complement['T'] = 'A';
        complement['t'] = 'a';
        complement['A'] = 'T';
        complement['a'] = 't';
    }


    /**
     * Run the final streaming version.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        new SteamingVersion().run("referenceGenes.list", "Ecoli");
    }

    @Override
    public void run(String referenceFile, String dir) throws IOException {
        long start = System.currentTimeMillis();
        List<Gene> referenceGenes = ParseReferenceGenes(referenceFile);
        List<DataContainer> dataContainers = new LinkedList<>();

        //read all the gene bank files in parallel.
        List<GenbankRecord> records = ListGenbankFiles(dir).parallelStream()
                .map(this::Parse)
                .collect(Collectors.toList());

        //collect all the work in the dataContainers list.
        for (GenbankRecord record : records) {
            for (Gene referenceGene : referenceGenes) {
                System.out.println(referenceGene.name);
                for (Gene gene : record.genes) {
                    dataContainers.add(new DataContainer(gene, referenceGene, record.nucleotides, referenceGene.name));
                }
            }
        }

        //preform all the work in parallel using streams.
        dataContainers.parallelStream()
                .filter(dataContainer -> Homologous(dataContainer.getGene().sequence, dataContainer.getReferenceGene().sequence))
                //find the Prediction in parallel
                .map(dataContainer -> {
                    NucleotideSequence upStreamRegion = GetUpstreamRegion(dataContainer.getNucleotides(), dataContainer.getGene());
                    return new PredictionContainer(PredictPromoter(upStreamRegion), dataContainer.getName());
                })
                //remove the null object in parallel.
                .filter(predictionContainer -> predictionContainer.prediction != null)
                //collect the result form the parallel work and put it into a sequntial list.
                .collect(Collectors.toList())
                //preform the remaining park in sequance.
                .forEach(predictionContainer -> {
                    consensus.get(predictionContainer.name).addMatch(predictionContainer.prediction);
                    consensus.get("all").addMatch(predictionContainer.prediction);
                });

        long end = System.currentTimeMillis();
        System.out.println(String.format("Run for: %s seconds", (end - start) / 1000L));

        for (Map.Entry<String, Sigma70Consensus> entry : consensus.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }


    @SneakyThrows
    private List<Gene> ParseReferenceGenes(String referenceFile)  {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(referenceFile)));
        List<Gene> referenceGenes = new ArrayList<>();
        while (true) {
            String name = reader.readLine();
            if (name == null) {
                break;
            }
            String sequence = reader.readLine();
            referenceGenes.add(new Gene(name, 0, 0, sequence));
            consensus.put(name, new Sigma70Consensus());
        }
        consensus.put("all", new Sigma70Consensus());
        reader.close();
        return referenceGenes;
    }

    private static List<String> ListGenbankFiles(String dir) {
        List<String> list = new ArrayList<>();
        ProcessDir(list, new File(dir));
        assert !list.isEmpty();
        return list;
    }

    private static void ProcessDir(List<String> list, File dir) {
        if (dir.exists()) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    ProcessDir(list, file);
                } else {
                    list.add(file.getPath());
                }
            }
        }
    }

    @SneakyThrows
    private GenbankRecord Parse(String file) {
        GenbankRecord record = new GenbankRecord();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        record.Parse(reader);
        reader.close();
        return record;
    }

    private boolean Homologous(PeptideSequence A, PeptideSequence B) {
        return SmithWatermanGotoh
                .align(
                        new Sequence(A.toString()),
                        new Sequence(B.toString()),
                        BLOSUM_62,
                        10f,
                        0.5f
                ).calculateScore() >= 60;
    }

    private NucleotideSequence GetUpstreamRegion(NucleotideSequence dna, Gene gene) {
        int upStreamDistance = 250;
        if (gene.location < upStreamDistance) {
            upStreamDistance = gene.location - 1;
        }

        if (gene.strand == 1) {
            return new NucleotideSequence(Arrays.copyOfRange(dna.bytes, gene.location - upStreamDistance - 1, gene.location - 1));
        } else {
            byte[] result = new byte[upStreamDistance];
            int reverseStart = dna.bytes.length - gene.location + upStreamDistance;

            for (int i = 0; i < upStreamDistance; i++) {
                result[i] = complement[dna.bytes[reverseStart - i]];
            }

            return new NucleotideSequence(result);
        }
    }

    private Match PredictPromoter(NucleotideSequence upStreamRegion) {
        return BioPatterns.getBestMatch(sigma70_pattern.get(), upStreamRegion.toString());
    }

    /**
     * A data object use in order to preform the parallel mapping.
     */
    @RequiredArgsConstructor
    private static class PredictionContainer {

        private final Match prediction;
        private final String name;
    }
}