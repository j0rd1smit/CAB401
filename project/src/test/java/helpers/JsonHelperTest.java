package helpers;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.JsonHelper;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import qut.Sequential;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 7-9-2018.
 */
public class JsonHelperTest {
    private static final String FILE_PATH = "jsonHelperTest.json";

    private Example example = new Example("Jordi", 24);

    /**
     * Verifies that
     */
    @Test
    void _simpleTest() throws IOException {

        JsonHelper.getJsonHelper().write(example, FILE_PATH);
        Example example1 = JsonHelper.getJsonHelper().read(Example.class, FILE_PATH);

        assertThat(example).isEqualTo(example1);
    }


    /**
     * Verifies that
     */
    @Test
    void _genericMapTest() throws IOException {
        Map<String, Example> expected = new HashMap<>();
        expected.put("name", example);

        JsonHelper.getJsonHelper().write(expected, FILE_PATH);

        Map<String, Example> result = JsonHelper.getJsonHelper().readGeneric(new TypeReference<Map<String, Example>>() {}, FILE_PATH);

        assertThat(result).isEqualTo(expected);
    }



    @AllArgsConstructor
    @ToString
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class Example {
        private String name;
        private int age;

    }
}
