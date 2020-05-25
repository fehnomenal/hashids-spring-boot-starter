package fehn.systems.boot.starter.hashids;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hashids")
@Getter
public class HashidsProperties {
    private String salt;
    private int minHashLength;
    private String alphabet;
}
