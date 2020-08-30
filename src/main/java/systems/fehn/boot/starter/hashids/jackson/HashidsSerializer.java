package systems.fehn.boot.starter.hashids.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import systems.fehn.boot.starter.hashids.Hashids;
import systems.fehn.boot.starter.hashids.HashidsProvider;

import java.io.IOException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HashidsSerializer extends StdScalarSerializer<Object> implements ContextualSerializer {
    private final TypeInformation typeInformation;
    private final HashidsProvider provider;
    private final Hashids annotation;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    public HashidsSerializer(final HashidsProvider provider) {
        super(Object.class);
        this.typeInformation = null;
        this.provider = provider;
        this.annotation = null;
    }

    private HashidsSerializer(final TypeInformation typeInformation,
                              final HashidsProvider provider,
                              final Hashids annotation) {
        super(Object.class);
        this.typeInformation = typeInformation;
        this.provider = provider;
        this.annotation = annotation;
    }

    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider prov, final BeanProperty property) {
        final var annotation = property.getAnnotation(Hashids.class);

        if (annotation != null) {
            final var typeInformation = TypeInformation.of(property.getType());

            if (typeInformation != null) {
                return new HashidsSerializer(typeInformation, provider, annotation);
            }
        }

        return null;
    }

    @Override
    public void serialize(final Object value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
        assert typeInformation != null;
        assert annotation != null;

        final var hashids = this.provider.getFromAnnotation(annotation);
        final var encoded = encode(value, hashids, typeInformation);

        gen.writeString(encoded);
    }


    public static String encode(final Object value, final org.hashids.Hashids hashids, final TypeInformation typeInformation) {
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
            return hashids.encode(realValues);
        } else {
            final long realValue;
            if (typeInformation.isLong()) {
                realValue = (Long) value;
            } else {
                realValue = (Integer) value;
            }
            return hashids.encode(realValue);
        }
    }
}
