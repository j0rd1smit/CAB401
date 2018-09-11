import com.fasterxml.jackson.core.type.TypeReference;
import helpers.JsonHelper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import qut.Sequential;
import qut.Sigma70Consensus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that functional is still working just like original sequential version.
 *
 * @author Jordi Smit on 7-9-2018.
 */
class FunctionalTest {

    /**
     * Verifies that small parts of system works.
     */
    @ParameterizedTest
    @CsvSource({
            "testLists/caiF.list",
            "testLists/carA.list",
            "testLists/fixB.list",
            "testLists/folA.list",
            "testLists/nhaA.list",
            "testLists/yaaY.list"
    })
    void _fastTest(String filePath) {
        String dir = "Ecoli/Escherichia_coli_BW2952_uid59391";
        Map<String, Sigma70Consensus> expected = readExpectedFile(TestResultsGenerator.formatOutputPath(filePath, dir));

        Map<String, Sigma70Consensus> result = calcConsensus(filePath, dir);

        assertThat(result).isEqualTo(expected);
    }


    @SneakyThrows
    private Map<String, Sigma70Consensus> calcConsensus(String filePath, String dir) {
        Sequential sequential = new Sequential();

        sequential.run(filePath, dir);
        return sequential.getConsensus();
    }

    @SneakyThrows
    private Map<String, Sigma70Consensus> readExpectedFile(String filePath) {
        return JsonHelper.getJsonHelper().readGeneric(new TypeReference<Map<String, Sigma70Consensus>>() {}, filePath);
    }

    /**
     * Verifies that entire system works.
     */
    @Test
    void _slowSystemTest() {
        String dir = "Ecoli";
        String filePath = "referenceGenes.list";
        Map<String, Sigma70Consensus> expected = readExpectedFile(TestResultsGenerator.formatOutputPath(filePath, dir));

        Map<String, Sigma70Consensus> result = calcConsensus(filePath, dir);

        assertThat(result).isEqualTo(expected);
    }

}
