
import lombok.Getter;
import qut.ISequential;
import qut.Sequential;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 28-9-2018.
 */
public class SequentialTest extends AbstractFunctionalTest {

    @Getter
    private final ISequential ISequential = new Sequential();
}
