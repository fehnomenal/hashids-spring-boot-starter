package systems.fehn.boot.starter.hashids;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hashids")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HashidsProperties {
    private String salt = null;
    private int minHashLength = 0;
    private String alphabet = null;
}
