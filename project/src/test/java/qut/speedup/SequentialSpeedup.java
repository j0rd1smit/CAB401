package qut.speedup;

import lombok.SneakyThrows;
import qut.Sequential;
import qut.SpeedupGraph;
import qut.final_versions.ExecutorServiceSynconzidedVersion;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 11-10-2018.
 */
public class SequentialSpeedup extends SpeedupGraph {
    @Override
    @SneakyThrows
    protected void runISequential(int nThreads, String dir, String filePath) {
        new Sequential().run(filePath, dir);
    }
}