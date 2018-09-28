package qut.versions;

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
 * TODO Explanation
 *
 * @author Jordi Smit on 28-9-2018.
 */
public class Functional implements ISequential<SynchronizedSigma70Consensus> {
    private static final Matrix BLOSUM_62 = BLOSUM62.Load();
    @Getter
    private Map<String, SynchronizedSigma70Consensus> consensus = new HashMap<>();
    private static ThreadLocal<Series> sigma70_pattern =ThreadLocal.withInitial(() ->  Sigma70Definition.getSeriesAll_Unanchored(0.7));
    private byte[] complement = new byte['z'];

    public Functional() {
        complement['C'] = 'G';
        complement['c'] = 'g';
        complement['G'] = 'C';
        complement['g'] = 'c';
        complement['T'] = 'A';
        complement['t'] = 'a';
        complement['A'] = 'T';
        complement['a'] = 't';
    }


    @SneakyThrows
    public static void main(String[] args) throws IOException {
        Thread.sleep(5000);
        new Functional().run("referenceGenes.list", "Ecoli");
    }

    @Override
    public void run(String referenceFile, String dir) throws IOException {
        long start = System.currentTimeMillis();
        List<Gene> referenceGenes = ParseReferenceGenes(referenceFile);
        List<GenbankRecord> genbankRecords = ListGenbankFiles(dir).parallelStream().map(this::Parse).collect(Collectors.toList());


        List<Tuple<GenbankRecord, Gene>> tuples = referenceGenes.parallelStream()
                .flatMap(referenceGene -> genbankRecords.stream().map(genbankRecord -> new Tuple<>(genbankRecord, referenceGene)))
                .collect(Collectors.toList());

        tuples.parallelStream()
                .forEach(tuple -> {
                   GenbankRecord record = tuple.left;
                   Gene referenceGene = tuple.right;
                   for (Gene gene : record.genes) {
                       if (Homologous(gene.sequence, referenceGene.sequence)) {
                           NucleotideSequence upStreamRegion = GetUpstreamRegion(record.nucleotides, gene);
                           Match prediction = PredictPromoter(upStreamRegion);

                           if (prediction != null) {
                               consensus.get(referenceGene.name).addMatch(prediction);
                               consensus.get("all").addMatch(prediction);
                           }
                       }
                   }
               });

        long end = System.currentTimeMillis();
        System.out.println(String.format("Run for: %s seconds", (end - start) / 1000L));

        for (Map.Entry<String, SynchronizedSigma70Consensus> entry : consensus.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }


    private List<Gene> ParseReferenceGenes(String referenceFile) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(referenceFile)));
        List<Gene> referenceGenes = new ArrayList<>();
        while (true) {
            String name = reader.readLine();
            if (name == null) {
                break;
            }
            String sequence = reader.readLine();
            referenceGenes.add(new Gene(name, 0, 0, sequence));
            consensus.put(name, new SynchronizedSigma70Consensus());
        }
        consensus.put("all", new SynchronizedSigma70Consensus());
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
                .align(new Sequence(A.toString()), new Sequence(B.toString()), BLOSUM_62, 10f, 0.5f)
                .calculateScore() >= 60;
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

    @RequiredArgsConstructor
    private static class Tuple<L, R> {
        private final L left;
        private final R right;

    }
}