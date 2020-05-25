package fehn.systems.boot.starter.hashids.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.stream.LongStream;

public class HashidsDeserializer extends StdScalarDeserializer<Object> implements ContextualDeserializer {
    private final TypeInformation typeInformation;
    private final Hashids hashids;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    public HashidsDeserializer(final Hashids hashids) {
        super(Object.class);
        this.typeInformation = null;
        this.hashids = hashids;
    }

    private HashidsDeserializer(final TypeInformation typeInformation,
                                final Hashids hashids) {
        super(Object.class);
        this.typeInformation = typeInformation;
        this.hashids = hashids;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        final var annotation = property.getAnnotation(fehn.systems.boot.starter.hashids.jackson.Hashids.class);

        if (annotation != null) {
            final var typeInformation = TypeInformation.of(property.getType());

            if (typeInformation != null) {
                return new HashidsDeserializer(typeInformation, hashids);
            }
        }

        return null;
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() == JsonToken.VALUE_STRING) {
            assert typeInformation != null;

            final var encoded = p.getValueAsString();
            final var decoded = hashids.decode(encoded);

            if (typeInformation.isArray()) {
                if (typeInformation.isBoxed()) {
                    if (typeInformation.isLong()) {
                        return LongStream.of(decoded).boxed().toArray(Long[]::new);
                    } else {
                        return LongStream.of(decoded).mapToInt(l -> (int) l).boxed().toArray(Integer[]::new);
                    }
                } else {
                    if (typeInformation.isLong()) {
                        return decoded;
                    } else {
                        return LongStream.of(decoded).mapToInt(l -> (int) l).toArray();
                    }
                }
            } else {
                if (typeInformation.isLong()) {
                    return decoded[0];
                } else {
                    return (int) decoded[0];
                }
            }
        }
        return null;
    }
}
