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
        if (properties.getAlphabet() != null) {
            return new Hashids(
                properties.getSalt(),
                properties.getMinHashLength(),
                properties.getAlphabet()
            );
        } else if (properties.getMinHashLength() > 0) {
            return new Hashids(properties.getSalt(), properties.getMinHashLength());
        } else if (properties.getSalt() != null) {
            return new Hashids(properties.getSalt());
        } else {
            return new Hashids();
        }
    }
}
