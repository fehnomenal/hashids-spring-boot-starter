package fehn.systems.boot.starter.hashids;

import org.hashids.Hashids;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(HashidsProperties.class)
public class HashidsAutoConfiguration {
    private final HashidsProperties properties;

    public HashidsAutoConfiguration(final HashidsProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public Hashids hashids() {
        return new Hashids(
            properties.getSalt(),
            properties.getMinHashLength(),
            properties.getAlphabet()
        );
    }
}
