package systems.fehn.boot.starter.hashids.jackson;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
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
