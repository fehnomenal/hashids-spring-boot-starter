package systems.fehn.boot.starter.hashids.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import systems.fehn.boot.starter.hashids.HashidsProperties;
import systems.fehn.boot.starter.hashids.HashidsProvider;

@Configuration
@ConditionalOnClass(ObjectMapper.class)
public class HashidsJacksonAutoConfiguration {
    @Bean
    public HashidsJacksonProvider hashidsJacksonProvider(final HashidsProperties properties,
                                                         final HashidsProvider provider) {
        return new HashidsJacksonProvider(properties, provider);
    }
}
