import com.sun.corba.se.impl.orbutil.ObjectWriter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 3-9-2018.
 */
public class VerifyResults {
    private static final String
            ORIGINAL_OUTPUT_FILE_PATH = "result_original.txt",
            TEST_OUTPUT_FILE_PATH = "result_original.txt";

    public static void main(String[] args) throws IOException {
        Map<String, String> original = readFile(ORIGINAL_OUTPUT_FILE_PATH);
        Map<String, String> testCase = readFile(TEST_OUTPUT_FILE_PATH);

        assertEqualsElements(original, testCase);
        assertEqualsElements(testCase, original);
        System.out.println("succesfull");
    }

    private static void assertEqualsElements(Map<String, String> original, Map<String, String> testCase) {
        for (String key : original.keySet()) {
            if (!testCase.containsKey(key)) {
                throw new RuntimeException(String.format("\nTestcase does not contain key: '%s'", key));
            }
            String originalValue = original.get(key);
            String testValue = testCase.get(key);

            if (!originalValue.equals(testValue)) {
                throw new RuntimeException(String.format("\nTestcase value for '%s' is:\n%s\nExpected:\n%s ", key, testValue, originalValue));
            }
        }
    }

    private static Map<String, String> readFile(String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path));
        Map<String, String> map = new HashMap<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split(";");
            map.put(split[0], split[1]);
        }

        return map;
    }
}
