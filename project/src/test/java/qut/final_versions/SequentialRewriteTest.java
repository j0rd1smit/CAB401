package qut.final_versions;

import lombok.Getter;
import qut.AbstractFunctionalTest;
import qut.ISequential;
import qut.Sequential;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 11-10-2018.
 */
public class SequentialRewriteTest extends AbstractFunctionalTest {

    @Getter
    private final ISequential iSequential = new SequentialRewrite();
}