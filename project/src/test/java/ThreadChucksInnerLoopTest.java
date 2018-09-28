import lombok.Getter;
import qut.ISequential;
import qut.versions.GeneStreamingOuterLoop;
import qut.versions.ThreadChucksInnerLoop;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 28-9-2018.
 */
public class ThreadChucksInnerLoopTest  extends AbstractFunctionalTest {

    @Getter
    private ISequential iSequential = new ThreadChucksInnerLoop(8);
}