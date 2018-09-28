import lombok.Getter;
import qut.ISequential;
import qut.versions.ThreadChucksInnerLoop;
import qut.versions.ThreadChucksMidLoop;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 28-9-2018.
 */
public class ThreadChucksMidLoopTest extends AbstractFunctionalTest {

    @Getter
    private ISequential iSequential = new ThreadChucksMidLoop(8);
}
