package qut.speedup;

import qut.ISequential;
import qut.SpeedupGraph;
import qut.final_versions.ExecutorServiceCollectorVersion;
import qut.final_versions.ExecutorServiceSynconzidedVersion;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 11-10-2018.
 */
public class ExecutorServiceSynconzidedVersionSpeedup extends SpeedupGraph {
    @Override
    protected void runISequential(int nThreads, String dir, String filePath) {
        new ExecutorServiceSynconzidedVersion(nThreads).run(filePath, dir);
    }
}