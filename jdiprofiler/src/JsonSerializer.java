import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonSerializer {
    public static String serialize(Object o) throws IOException {
        var mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }
}
