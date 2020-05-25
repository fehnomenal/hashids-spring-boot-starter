package fehn.systems.boot.starter.hashids.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HashidsSerializer extends StdScalarSerializer<Object> implements ContextualSerializer {
    private final TypeInformation typeInformation;
    private final Hashids hashids;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    public HashidsSerializer(final Hashids hashids) {
        super(Object.class);
        this.typeInformation = null;
        this.hashids = hashids;
    }

    private HashidsSerializer(final TypeInformation typeInformation,
                              final Hashids hashids) {
        super(Object.class);
        this.typeInformation = typeInformation;
        this.hashids = hashids;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        final var annotation = property.getAnnotation(fehn.systems.boot.starter.hashids.jackson.Hashids.class);

        if (annotation != null) {
            final var typeInformation = TypeInformation.of(property.getType());

            if (typeInformation != null) {
                return new HashidsSerializer(typeInformation, hashids);
            }
        }

        return null;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        assert typeInformation != null;

        final String encoded;
        if (typeInformation.isArray()) {
            final long[] realValues;
            if (typeInformation.isBoxed()) {
                if (typeInformation.isLong()) {
                    realValues = Stream.of((Long[]) value).mapToLong(Long::longValue).toArray();
                } else {
                    realValues = Stream.of((Integer[]) value).mapToLong(Integer::longValue).toArray();
                }
            } else {
                if (typeInformation.isLong()) {
                    realValues = (long[]) value;
                } else {
                    realValues = IntStream.of((int[]) value).mapToLong(Long::valueOf).toArray();
                }
            }
            encoded = hashids.encode(realValues);
        } else {
            final long realValue;
            if (typeInformation.isLong()) {
                realValue = (Long) value;
            } else {
                realValue = (Integer) value;
            }
            encoded = hashids.encode(realValue);
        }

        gen.writeString(encoded);
    }
}
