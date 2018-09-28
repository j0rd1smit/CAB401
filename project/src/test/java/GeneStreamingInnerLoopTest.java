import lombok.Getter;
import qut.versions.Functional;
import qut.ISequential;
import qut.versions.GeneStreamingInnerLoop;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 28-9-2018.
 */
public class GeneStreamingInnerLoopTest extends AbstractFunctionalTest {

    @Getter
    private ISequential iSequential = new GeneStreamingInnerLoop();
}