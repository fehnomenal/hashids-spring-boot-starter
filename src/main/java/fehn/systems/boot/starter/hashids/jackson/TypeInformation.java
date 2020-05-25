package fehn.systems.boot.starter.hashids.jackson;

import com.fasterxml.jackson.databind.JavaType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TypeInformation {
    private final boolean isArray;
    private final boolean isBoxed;
    private final boolean isLong;

    public static TypeInformation of(final JavaType type) {
        final boolean isArray;
        final boolean isBoxed;
        final boolean isLong;

        if (type.isArrayType()) {
            isArray = true;
            isBoxed = !type.getContentType().isPrimitive();

            if (isIntType(type.getContentType())) {
                isLong = false;
            } else if (isLongType(type.getContentType())) {
                isLong = true;
            } else {
                return null;
            }
        } else {
            isArray = false;
            isBoxed = !type.isPrimitive();

            if (isIntType(type)) {
                isLong = false;
            } else if (isLongType(type)) {
                isLong = true;
            } else {
                return null;
            }
        }

        return new TypeInformation(isArray, isBoxed, isLong);
    }

    private static boolean isIntType(final JavaType type) {
        return type.hasRawClass(int.class) || type.hasRawClass(Integer.class);
    }

    private static boolean isLongType(final JavaType type) {
        return type.hasRawClass(long.class) || type.hasRawClass(Long.class);
    }
}
