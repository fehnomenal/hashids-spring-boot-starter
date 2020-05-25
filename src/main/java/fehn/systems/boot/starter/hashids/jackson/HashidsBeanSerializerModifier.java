package fehn.systems.boot.starter.hashids.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.List;

class HashidsBeanSerializerModifier extends BeanSerializerModifier {
    private final org.hashids.Hashids hashids;

    public HashidsBeanSerializerModifier(final org.hashids.Hashids hashids) {
        this.hashids = hashids;
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(final SerializationConfig config,
                                                     final BeanDescription beanDesc,
                                                     final List<BeanPropertyWriter> beanProperties) {
        for (int i = 0; i < beanProperties.size(); i++) {
            final var beanPropertyWriter = beanProperties.get(i);

            final var annotation = beanPropertyWriter.getAnnotation(Hashids.class);
            if (annotation != null) {
                final var type = beanPropertyWriter.getType();
                final boolean isArray, isBoxed, isLong;

                if (type.isArrayType()) {
                    isArray = true;
                    isBoxed = !type.getContentType().isPrimitive();

                    if (isIntType(type.getContentType())) {
                        isLong = false;
                    } else if (isLongType(type.getContentType())) {
                        isLong = true;
                    } else {
                        continue;
                    }
                } else {
                    isArray = false;
                    isBoxed = !type.isPrimitive();

                    if (isIntType(type)) {
                        isLong = false;
                    } else if (isLongType(type)) {
                        isLong = true;
                    } else {
                        continue;
                    }
                }

                beanProperties.set(i, new HashidsEncodingWriter(beanPropertyWriter, hashids, isArray, isBoxed, isLong));
            }
        }

        return beanProperties;
    }


    private static boolean isIntType(final JavaType type) {
        return type.hasRawClass(int.class) || type.hasRawClass(Integer.class);
    }

    private static boolean isLongType(final JavaType type) {
        return type.hasRawClass(long.class) || type.hasRawClass(Long.class);
    }
}
