import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.sun.jdi.Method;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CallContainer implements JsonSerializable {
    private Map<Method, Integer> map;

    public CallContainer() {
        map = new HashMap<>();
    }

    public void call(Method method) {
        if (map.containsKey(method)) {
            map.put(method, map.get(method) + 1);
            return;
        }
        map.put(method, 1);
    }

    public void print() {
        map.forEach((method, calls) -> System.out.println(method.declaringType().name() + "." + method.name() + "()" + " called " + calls + " times"));
    }

    @Override
    public void serialize(JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeFieldName("methods");
        jsonGenerator.writeStartArray();

        map.forEach((method, calls) -> {
            try {
                jsonGenerator.writeStartObject();

                jsonGenerator.writeFieldName("name");
                jsonGenerator.writeString(method.declaringType().name() + "." + method.name() + "()");

                jsonGenerator.writeFieldName("calls");
                jsonGenerator.writeNumber(calls);

                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }


    @Override
    public void serializeWithType(JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException {
        serialize(jsonGenerator, serializerProvider);
    }
}