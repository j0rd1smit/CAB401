package helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that {@link JsonHelper} works as expected.
 *
 * @author Jordi Smit on 7-9-2018.
 */
class JsonHelperTest {
    private static final String FILE_PATH = "jsonHelperTest.json";

    private Example example = new Example("Jordi", 24);

    /**
     * Verifies that a simple class be write and read from file.
     */
    @Test
    void _simpleTest() throws IOException {

        JsonHelper.getJsonHelper().write(example, FILE_PATH);
        Example example1 = JsonHelper.getJsonHelper().read(Example.class, FILE_PATH);

        assertThat(example).isEqualTo(example1);
    }


    /**
     * Verifies that a generic class be write and read from file.
     */
    @Test
    void _genericMapTest() throws IOException {
        Map<String, Example> expected = new HashMap<>();
        expected.put("name", example);

        JsonHelper.getJsonHelper().write(expected, FILE_PATH);

        Map<String, Example> result = JsonHelper.getJsonHelper().readGeneric(new TypeReference<Map<String, Example>>() {}, FILE_PATH);

        assertThat(result).isEqualTo(expected);
    }


    /**
     * An test case class
     */
    @AllArgsConstructor
    @ToString
    @NoArgsConstructor
    @EqualsAndHashCode
    static class Example {
        private String name;
        private int age;

    }
}
