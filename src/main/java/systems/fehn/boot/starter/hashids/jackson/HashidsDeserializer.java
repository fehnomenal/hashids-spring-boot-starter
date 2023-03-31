package systems.fehn.boot.starter.hashids.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import systems.fehn.boot.starter.hashids.Hashids;
import systems.fehn.boot.starter.hashids.HashidsProvider;

import java.io.IOException;
import java.util.stream.LongStream;

public class HashidsDeserializer extends StdScalarDeserializer<Object> implements ContextualDeserializer {
    private final TypeInformation typeInformation;
    private final HashidsProvider provider;
    private final Hashids annotation;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    public HashidsDeserializer(final HashidsProvider provider) {
        super(Object.class);
        this.provider = provider;
        this.typeInformation = null;
        this.annotation = null;
    }

    private HashidsDeserializer(final TypeInformation typeInformation,
                                final HashidsProvider provider,
                                final Hashids annotation) {
        super(Object.class);
        this.typeInformation = typeInformation;
        this.provider = provider;
        this.annotation = annotation;
    }

    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext ctxt, final BeanProperty property) {
        final Hashids annotation = property.getAnnotation(Hashids.class);

        if (annotation != null) {
            final TypeInformation typeInformation = TypeInformation.of(property.getType());

            if (typeInformation != null) {
                return new HashidsDeserializer(typeInformation, provider, annotation);
            }
        }

        return null;
    }

    @Override
    public Object deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        if (p.currentToken() == JsonToken.VALUE_STRING) {
            assert typeInformation != null;
            assert annotation != null;

            final org.hashids.Hashids hashids = provider.getFromAnnotation(annotation);

            final String encoded = p.getValueAsString();

            return decode(encoded, hashids, typeInformation);
        }
        return null;
    }


    public static Object decode(final String encoded, final org.hashids.Hashids hashids, final TypeInformation typeInformation) {
        final long[] decoded = hashids.decode(encoded);

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
}
