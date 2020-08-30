package systems.fehn.boot.starter.hashids;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import systems.fehn.boot.starter.hashids.jackson.HashidsDeserializer;
import systems.fehn.boot.starter.hashids.jackson.HashidsSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@JacksonAnnotationsInside
@JsonSerialize(using = HashidsSerializer.class)
@JsonDeserialize(using = HashidsDeserializer.class)
public @interface Hashids {
    String SALT_FROM_PROPERTIES = "salt_from_properties";
    int MIN_LENGTH_FROM_PROPERTIES = -1;
    String ALPHABET_FROM_PROPERTIES = "alphabet_from_properties";


    String salt() default SALT_FROM_PROPERTIES;

    int minHashLength() default MIN_LENGTH_FROM_PROPERTIES;

    String alphabet() default ALPHABET_FROM_PROPERTIES;
}
