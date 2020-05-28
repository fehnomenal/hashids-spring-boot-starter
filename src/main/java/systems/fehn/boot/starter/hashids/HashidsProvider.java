package systems.fehn.boot.starter.hashids;

import java.util.LinkedHashMap;
import java.util.Map;

public class HashidsProvider {
    private final Map<HashidsProperties, org.hashids.Hashids> lruCache = new LinkedHashMap<>(10, .75f, true) {
        @Override
        protected boolean removeEldestEntry(final Map.Entry<HashidsProperties, org.hashids.Hashids> eldest) {
            return size() > 10;
        }
    };

    private final HashidsProperties properties;

    public HashidsProvider(final HashidsProperties properties) {
        this.properties = properties;
    }


    public org.hashids.Hashids getHashids(final String salt,
                                          final int minHashLength,
                                          final String alphabet) {
        final var key = new HashidsProperties(salt, minHashLength, alphabet);
        {
            final var hashids = lruCache.get(key);
            if (hashids != null) {
                return hashids;
            }
        }

        final org.hashids.Hashids hashids;
        if (alphabet == null) {
            hashids = new org.hashids.Hashids(salt, minHashLength);
        } else {
            hashids = new org.hashids.Hashids(salt, minHashLength, alphabet);
        }

        lruCache.put(key, hashids);

        return hashids;
    }

    public org.hashids.Hashids getFromAnnotation(final Hashids annotation) {
        final var salt = annotation.salt().equals(Hashids.SALT_FROM_PROPERTIES) ?
            properties.getSalt() : annotation.salt();
        final var minHashLength = annotation.minHashLength() == Hashids.MIN_LENGTH_FROM_PROPERTIES ?
            properties.getMinHashLength() : annotation.minHashLength();
        final var alphabet = annotation.alphabet().equals(Hashids.ALPHABET_FROM_PROPERTIES) ?
            properties.getAlphabet() : annotation.alphabet();

        return getHashids(salt, minHashLength, alphabet);
    }
}
