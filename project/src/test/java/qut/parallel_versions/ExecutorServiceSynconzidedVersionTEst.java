package qut.parallel_versions;

import lombok.Getter;
import qut.AbstractFunctionalTest;
import qut.ISequential;

/**
 * Run the AbstractFunctionalTest for the ExecutorServiceSynconzidedVersion.
 *
 * @author Jordi Smit on 11-10-2018.
 */
public class ExecutorServiceSynconzidedVersionTEst extends AbstractFunctionalTest {
        @Getter
        private final ISequential iSequential = new ExecutorServiceSynconzidedVersion(8);
}

