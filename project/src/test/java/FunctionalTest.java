import com.fasterxml.jackson.core.type.TypeReference;
import helpers.JsonHelper;
import org.junit.jupiter.api.Test;
import qut.Sequential;
import qut.Sigma70Consensus;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
        Sequential sequential = new Sequential();

        sequential.run("testLists/nhaA.list", "Ecoli");
        Map<String, Sigma70Consensus> result = sequential.getConsensus();

        Map<String, Sigma70Consensus> expected = JsonHelper.getJsonHelper().readGeneric(new TypeReference<Map<String, Sigma70Consensus>>() {}, "consensus.json");

        assertThat(result).isEqualTo(expected);
    }

    /**
     * Verifies that
     */
    @Test
    void _Test() {
        assertThat(true).isFalse();
    }
}
