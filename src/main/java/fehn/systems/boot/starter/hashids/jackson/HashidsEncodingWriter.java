package fehn.systems.boot.starter.hashids.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import org.hashids.Hashids;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HashidsEncodingWriter extends BeanPropertyWriter {
    private final Hashids hashids;
    private final boolean isArray;
    private final boolean isBoxed;
    private final boolean isLong;

    public HashidsEncodingWriter(final BeanPropertyWriter base,
                                 final Hashids hashids,
                                 final boolean isArray,
                                 final boolean isBoxed,
                                 final boolean isLong) {
        super(base);
        this.hashids = hashids;
        this.isArray = isArray;
        this.isBoxed = isBoxed;
        this.isLong = isLong;
    }

    @Override
    public void serializeAsField(final Object bean, final JsonGenerator gen, final SerializerProvider prov) throws Exception {
        final Object value = (_accessorMethod == null) ? _field.get(bean) :
            _accessorMethod.invoke(bean, (Object[]) null);

        final String encoded;
        if (isArray) {
            final long[] realValues;
            if (isBoxed) {
                if (isLong) {
                    realValues = Stream.of((Long[]) value).mapToLong(Long::longValue).toArray();
                } else {
                    realValues = Stream.of((Integer[]) value).mapToLong(Integer::longValue).toArray();
                }
            } else {
                if (isLong) {
                    realValues = (long[]) value;
                } else {
                    realValues = IntStream.of((int[]) value).mapToLong(Long::valueOf).toArray();
                }
            }
            encoded = hashids.encode(realValues);
        } else {
            final long realValue;
            if (isLong) {
                realValue = (Long) value;
            } else {
                realValue = (Integer) value;
            }
            encoded = hashids.encode(realValue);
        }

        gen.writeFieldName(_name);
        gen.writeString(encoded);
    }
}
