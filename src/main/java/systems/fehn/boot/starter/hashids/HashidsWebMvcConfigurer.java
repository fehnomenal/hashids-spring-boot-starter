package systems.fehn.boot.starter.hashids;

import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class HashidsWebMvcConfigurer implements WebMvcConfigurer {
    private final HashidsProvider provider;

    public HashidsWebMvcConfigurer(final HashidsProvider provider) {
        this.provider = provider;
    }

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addFormatterForFieldAnnotation(new HashidsAnnotationFormatterFactory(provider));
    }
}
