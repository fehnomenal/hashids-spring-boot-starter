package systems.fehn.boot.starter.hashids.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HashidsSerializer extends StdScalarSerializer<Object> implements ContextualSerializer {
    private final TypeInformation typeInformation;
    private final HashidsJacksonProvider provider;
    private final Hashids annotation;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    public HashidsSerializer(final HashidsJacksonProvider provider) {
        super(Object.class);
        this.typeInformation = null;
        this.provider = provider;
        this.annotation = null;
    }

    private HashidsSerializer(final TypeInformation typeInformation,
                              final HashidsJacksonProvider provider,
                              final Hashids annotation) {
        super(Object.class);
        this.typeInformation = typeInformation;
        this.provider = provider;
        this.annotation = annotation;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        final var annotation = property.getAnnotation(systems.fehn.boot.starter.hashids.jackson.Hashids.class);

        if (annotation != null) {
            final var typeInformation = TypeInformation.of(property.getType());

            if (typeInformation != null) {
                return new HashidsSerializer(typeInformation, provider, annotation);
            }
        }

        return null;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        assert typeInformation != null;
        assert annotation != null;

        final var hashids = this.provider.getFromAnnotation(annotation);

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
