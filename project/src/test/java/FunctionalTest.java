import org.junit.jupiter.api.Test;
import qut.Sequential;

import java.io.IOException;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 7-9-2018.
 */
public class FunctionalTest {
    /**
     * Verifies that
     */
    @Test
    void _runTest() throws IOException {
        new Sequential().run("referenceGenes.list", "Ecoli");
    }
}
