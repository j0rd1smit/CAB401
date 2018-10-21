package helpers;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.File;
import java.io.IOException;

/**
 * A Object mapper that maps the serialized json version to in memory version for testing purposes.
 *
 * @author Jordi Smit on 7-9-2018.
 */
public class JsonHelper {
    private static final JsonHelper JSON_HELPER = new JsonHelper();

    private ObjectMapper mapper;

    private JsonHelper() {
        this.mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    }

    /**
     * @return The Singleton instance.
     */
    public static synchronized JsonHelper getJsonHelper() {
        return JSON_HELPER;
    }

    /**
     * Writes the object to file.
     * @param object The object.
     * @param filePath The file.
     * @throws IOException
     */
    public synchronized void write(Object object, String filePath) throws IOException {
        mapper.writeValue(new File(filePath), object);
    }

    /**
     * Reads a object form file.
     * @param clazz The class to cast it to.
     * @param filePath The file.
     * @param <E> The class generic.
     * @return The resulting object.
     * @throws IOException
     */
    public synchronized <E> E read(Class<E> clazz, String filePath) throws IOException {
        return mapper.readValue(new File(filePath), clazz);
    }

    /**
     * Reads a generic object from file.
     * @param typeReference The generic type.
     * @param filePath The file.
     * @param <E> The class generic.
     * @return The resulting object.
     * @throws IOException
     */
    public synchronized <E> E readGeneric(TypeReference<E> typeReference, String filePath) throws IOException {
        return mapper.readValue(new File(filePath), typeReference);
    }
}
