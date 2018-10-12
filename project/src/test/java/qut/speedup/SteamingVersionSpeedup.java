package qut.speedup;

import lombok.SneakyThrows;
import qut.SpeedupGraph;
import qut.final_versions.ExecutorServiceSynconzidedVersion;
import qut.final_versions.SteamingVersion;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 11-10-2018.
 */
public class SteamingVersionSpeedup extends SpeedupGraph {
    @Override
    @SneakyThrows
    protected void runISequential(int nThreads, String dir, String filePath) {
        ForkJoinPool customThreadPool = new ForkJoinPool(nThreads);
        customThreadPool.submit(
                () -> {
                    try {
                        new SteamingVersion().run(filePath, dir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).get();
        ;
    }
}
