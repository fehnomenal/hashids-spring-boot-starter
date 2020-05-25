package fehn.systems.boot.starter.hashids.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import fehn.systems.boot.starter.hashids.HashidsProperties;
import fehn.systems.boot.starter.hashids.HashidsProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ObjectMapper.class)
public class HashidsJacksonAutoConfiguration {
    @Bean
    public HashidsJacksonProvider hashidsJacksonProvider(final HashidsProperties properties,
                                                         final HashidsProvider provider) {
        return new HashidsJacksonProvider(properties, provider);
    }
}
