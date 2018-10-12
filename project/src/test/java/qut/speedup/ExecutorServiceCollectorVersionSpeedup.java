package qut.speedup;

import qut.ISequential;
import qut.SpeedupGraph;
import qut.final_versions.ExecutorServiceCollectorVersion;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 11-10-2018.
 */
public class ExecutorServiceCollectorVersionSpeedup extends SpeedupGraph {
    @Override
    protected void runISequential(int nThreads, String dir, String filePath) {
        new ExecutorServiceCollectorVersion(nThreads).run(filePath, dir);
    }
}
