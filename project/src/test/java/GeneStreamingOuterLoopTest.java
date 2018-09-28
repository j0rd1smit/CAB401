import lombok.Getter;
import qut.ISequential;
import qut.versions.GeneStreamingOuterLoop;


/**
 * TODO Explanation
 *
 * @author Jordi Smit on 28-9-2018.
 */
public class GeneStreamingOuterLoopTest extends AbstractFunctionalTest {

    @Getter
    private ISequential iSequential = new GeneStreamingOuterLoop();
}
