package systems.fehn.boot.starter.hashids.jackson;

import systems.fehn.boot.starter.hashids.HashidsProperties;
import systems.fehn.boot.starter.hashids.HashidsProvider;

public class HashidsJacksonProvider {
    private final HashidsProperties properties;
    private final HashidsProvider provider;

    public HashidsJacksonProvider(final HashidsProperties properties,
                                  final HashidsProvider provider) {
        this.properties = properties;
        this.provider = provider;
    }

    public org.hashids.Hashids getFromAnnotation(final Hashids annotation) {
        final var salt = annotation.salt().equals(Hashids.SALT_FROM_PROPERTIES) ?
            properties.getSalt() : annotation.salt();
        final var minHashLength = annotation.minHashLength() == Hashids.MIN_LENGTH_FROM_PROPERTIES ?
            properties.getMinHashLength() : annotation.minHashLength();
        final var alphabet = annotation.alphabet().equals(Hashids.ALPHABET_FROM_PROPERTIES) ?
            properties.getAlphabet() : annotation.alphabet();

        return provider.getHashids(salt, minHashLength, alphabet);
    }
}
