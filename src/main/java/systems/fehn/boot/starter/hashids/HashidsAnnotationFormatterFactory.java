package systems.fehn.boot.starter.hashids;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import systems.fehn.boot.starter.hashids.jackson.HashidsDeserializer;
import systems.fehn.boot.starter.hashids.jackson.HashidsSerializer;
import systems.fehn.boot.starter.hashids.jackson.TypeInformation;

import java.util.HashSet;
import java.util.Set;

public class HashidsAnnotationFormatterFactory implements AnnotationFormatterFactory<Hashids> {
    private final HashidsProvider provider;

    public HashidsAnnotationFormatterFactory(final HashidsProvider provider) {
        this.provider = provider;
    }

    @Override
    public Set<Class<?>> getFieldTypes() {
        final Set<Class<?>> set = new HashSet<>();
        set.add(int.class);
        set.add(Integer.class);
        set.add(long.class);
        set.add(Long.class);
        set.add(int[].class);
        set.add(Integer[].class);
        set.add(long[].class);
        set.add(Long[].class);
        return set;
    }

    @Override
    public Printer<?> getPrinter(final Hashids annotation, final Class<?> fieldType) {
        final org.hashids.Hashids hashids = provider.getFromAnnotation(annotation);
        final TypeInformation typeInformation = getTypeInformation(fieldType);
        return (object, locale) -> HashidsSerializer.encode(object, hashids, typeInformation);
    }

    @Override
    public Parser<?> getParser(final Hashids annotation, final Class<?> fieldType) {
        final org.hashids.Hashids hashids = provider.getFromAnnotation(annotation);
        final TypeInformation typeInformation = getTypeInformation(fieldType);
        return (text, locale) -> HashidsDeserializer.decode(text, hashids, typeInformation);
    }


    private static TypeInformation getTypeInformation(final Class<?> fieldType) {
        if (fieldType.isArray()) {
            return new TypeInformation(
                true,
                fieldType == Integer[].class || fieldType == Long[].class,
                fieldType == long[].class || fieldType == Long[].class
            );
        } else {
            return new TypeInformation(
                false,
                fieldType == Integer.class || fieldType == Long.class,
                fieldType == long.class || fieldType == Long.class
            );
        }
    }
}
