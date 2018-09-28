import lombok.Getter;
import qut.ISequential;
import qut.versions.Functional;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 28-9-2018.
 */
public class FunctionalVersionTest extends AbstractFunctionalTest {

    @Getter
    private ISequential iSequential = new Functional();
}
