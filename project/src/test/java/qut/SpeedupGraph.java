package qut;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 11-10-2018.
 */
public abstract class SpeedupGraph {

    protected abstract void runISequential(int nThreads, String dir, String filePath);


    /**
     * Verifies that
     */
    @Test
    void _Test() throws IOException {
        String dir = "Ecoli";
        String filePath = "referenceGenes.list";
        List<Long> time = new LinkedList<>();

        for (int i = 0; i < 8; i++) {
            int nThreads = i + 1;
            System.out.println(String.format("Starting on %s threads", nThreads));
            long start = System.currentTimeMillis();
            runISequential(nThreads, dir, filePath);
            long end = System.currentTimeMillis();
            time.add(end - start);

            System.out.println(String.format("Done on %s threads it took: %s", nThreads, start - end));
        }

        System.out.println("results: ");
        System.out.println(time);
    }
}
