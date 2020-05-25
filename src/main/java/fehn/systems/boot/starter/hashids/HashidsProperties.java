package fehn.systems.boot.starter.hashids;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hashids")
@Getter
@Setter
public class HashidsProperties {
    private String salt = null;
    private int minHashLength = 0;
    private String alphabet = null;
}
