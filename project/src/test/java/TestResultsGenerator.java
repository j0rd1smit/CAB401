import helpers.JsonHelper;
import lombok.SneakyThrows;
import qut.Sequential;
import qut.Sigma70Consensus;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Used to generate the sequential data output in a JSON format.
 * Which can be stored for later comparisons.
 * @author Jordi Smit on 11-9-2018.
 */
public class TestResultsGenerator {

    /**
     * Used to generate the sequential data output in a JSON format.
     * Which can be stored for later comparisons.
     * @param args None
     */
    public static void main(String[] args)  {
        //generateFastTest();
        //generateSystemTests();
    }

    /**
     * Generates small tests cases.
     * Which can be used for during development.
     */
    private static void generateFastTest() {
        String dir = "Ecoli/Escherichia_coli_BW2952_uid59391";

        List<String> filePaths = Arrays.asList(
                "testLists/caiF.list",
                "testLists/carA.list",
                "testLists/fixB.list",
                "testLists/folA.list",
                "testLists/nhaA.list",
                "testLists/yaaY.list"
        );

        generateTest(dir, filePaths);
    }

    @SneakyThrows
    private static void generateTest(String dir, List<String> filePaths) {
        for (String filePath : filePaths) {
            Sequential sequential = new Sequential();
            sequential.run(filePath, dir);
            Map<String, Sigma70Consensus> result = sequential.getConsensus();

            String outputPath = formatOutputPath(filePath, dir);
            JsonHelper.getJsonHelper().write(result, outputPath);
        }
    }

    /**
     *
     * @param filePathToList The list being used.
     * @param dir The Ecoli dir being used.
     * @return The file location of the stored result.
     */
    static String formatOutputPath(String filePathToList, String dir) {
        return String.format("expected_output/%s/%s", dir, filePathToList.replace(".list", ".json"));
    }

    /**
     * Generates full system test cases.
     * Which can be used for continues integration verification.
     */
    private static void generateSystemTests() {
        String dir = "Ecoli";

        List<String> filePaths = Arrays.asList(
                "referenceGenes.list"
        );

        generateTest(dir, filePaths);
    }
}
