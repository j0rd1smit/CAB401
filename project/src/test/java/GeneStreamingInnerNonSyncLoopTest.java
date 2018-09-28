import lombok.Getter;
import qut.ISequential;
import qut.versions.GeneStreamingMidLoop;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 28-9-2018.
 */
public class GeneStreamingInnerNonSyncLoopTest extends AbstractFunctionalTest {

    @Getter
    private ISequential iSequential = new GeneStreamingMidLoop();
}