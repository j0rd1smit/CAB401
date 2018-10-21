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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The ExecutorServiceSynconzided version.
 *
 * @author Jordi Smit on 11-10-2018.
 */
@SuppressWarnings("ALL")
public class ExecutorServiceSynconzidedVersion implements ISequential {
    private static final Matrix BLOSUM_62 = BLOSUM62.Load();
    @Getter
    private Map<String, Sigma70Consensus> consensus = new HashMap<>();
    private static ThreadLocal<Series> sigma70_pattern = ThreadLocal.withInitial(() -> Sigma70Definition.getSeriesAll_Unanchored(0.7));
    private byte[] complement = new byte['z'];

    private final int nThreads;

    public ExecutorServiceSynconzidedVersion(int nThreads) {
        this.nThreads = nThreads;
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
     * Run the ExecutorServiceSynconzided version.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new ExecutorServiceSynconzidedVersion(8).run("referenceGenes.list", "Ecoli");
    }

    @Override
    @SneakyThrows
    public void run(String referenceFile, String dir) {
        long start = System.currentTimeMillis();
        List<Gene> referenceGenes = ParseReferenceGenes(referenceFile);
        List<GenbankRecord> records = new ArrayList<>();
        List<DataContainer> dataContainers = new LinkedList<>();

        //read all the genbank files.
        for (String filename : ListGenbankFiles(dir)) {
            records.add(Parse(filename));
        }

        //collect all the work in the dataContainers list.
        for (GenbankRecord record : records) {
            for (Gene referenceGene : referenceGenes) {
                System.out.println(referenceGene.name);
                for (Gene gene : record.genes) {
                    dataContainers.add(new DataContainer(gene, referenceGene, record.nucleotides, referenceGene.name));
                }
            }
        }
        //create a thread pool.
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        //preform the work in parallel.
        executorService.invokeAll(dataContainers);

        long end = System.currentTimeMillis();
        System.out.println(String.format("Run for: %s seconds", (end - start) / 1000L));

        for (Map.Entry<String, Sigma70Consensus> entry : consensus.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        //shutdown the thread pool.
        executorService.shutdown();
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

    private GenbankRecord Parse(String file) throws IOException {
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
     * The syncornization lock.
     */
    private static final Object LOCK = new Object();

    /**
     * The data container.
     */
    @Getter
    @RequiredArgsConstructor
    private class DataContainer implements Callable<Void> {
        private final Gene gene;
        private final Gene referenceGene;
        private final NucleotideSequence nucleotides;
        private final String name;

        /**
         * The work to preformed in parallel.
         * @return The prediction result.
         * @throws Exception
         */
        @Override
        public Void call() throws Exception {
            if (Homologous(gene.sequence, referenceGene.sequence)) {
                NucleotideSequence upStreamRegion = GetUpstreamRegion(nucleotides, gene);
                Match prediction = PredictPromoter(upStreamRegion);

                if (prediction != null) {
                    synchronized (LOCK) {
                        consensus.get(name).addMatch(prediction);
                        consensus.get("all").addMatch(prediction);
                    }
                }
            }
            return null;
        }
    }
}