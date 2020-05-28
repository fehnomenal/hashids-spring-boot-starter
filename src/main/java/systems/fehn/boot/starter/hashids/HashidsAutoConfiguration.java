package systems.fehn.boot.starter.hashids;

import org.hashids.Hashids;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(HashidsWebMvcConfigurer.class)
@EnableConfigurationProperties(HashidsProperties.class)
public class HashidsAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public Hashids hashids(final HashidsProperties properties,
                           final HashidsProvider provider) {
        return provider.getHashids(
            properties.getSalt(),
            properties.getMinHashLength(),
            properties.getAlphabet()
        );
    }

    @Bean
    public HashidsProvider hashidsProvider(final HashidsProperties properties) {
        return new HashidsProvider(properties);
    }
}
