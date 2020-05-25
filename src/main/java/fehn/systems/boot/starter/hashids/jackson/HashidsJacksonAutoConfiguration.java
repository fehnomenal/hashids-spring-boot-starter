package fehn.systems.boot.starter.hashids.jackson;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.hashids.Hashids;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ObjectMapper.class)
public class HashidsJacksonAutoConfiguration {
    @Bean
    public Module module(final HashidsBeanSerializerModifier beanSerializerModifier) {
        final var module = new SimpleModule();

        module.setSerializerModifier(beanSerializerModifier);

        return module;
    }

    @Bean
    public HashidsBeanSerializerModifier hashidsBeanSerializerModifier(final Hashids hashids) {
        return new HashidsBeanSerializerModifier(hashids);
    }
}
