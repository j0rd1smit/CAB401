package helpers;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.File;
import java.io.IOException;

/**
 * TODO Explanation
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

    public static synchronized JsonHelper getJsonHelper() {
        return JSON_HELPER;
    }

    public synchronized void write(Object object, String filePath) throws IOException {
        mapper.writeValue(new File(filePath), object);
    }

    public synchronized <E> E read(Class<E> clazz, String filePath) throws IOException {
        return mapper.readValue(new File(filePath), clazz);
    }

    public synchronized <E> E readGeneric(TypeReference<E> typeReference, String filePath) throws IOException {
        return mapper.readValue(new File(filePath), typeReference);
    }
}
