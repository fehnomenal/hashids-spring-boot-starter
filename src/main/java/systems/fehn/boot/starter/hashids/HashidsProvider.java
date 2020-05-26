package systems.fehn.boot.starter.hashids;

import org.hashids.Hashids;

import java.util.LinkedHashMap;
import java.util.Map;

public class HashidsProvider {
    private final Map<HashidsProperties, Hashids> lruCache = new LinkedHashMap<>(10, .75f, true) {
        @Override
        protected boolean removeEldestEntry(final Map.Entry<HashidsProperties, Hashids> eldest) {
            return size() > 10;
        }
    };


    public Hashids getHashids(final String salt,
                              final int minHashLength,
                              final String alphabet) {
        final var key = new HashidsProperties(salt, minHashLength, alphabet);
        {
            final var hashids = lruCache.get(key);
            if (hashids != null) {
                return hashids;
            }
        }

        final Hashids hashids;
        if (alphabet == null) {
            hashids = new Hashids(salt, minHashLength);
        } else {
            hashids = new Hashids(salt, minHashLength, alphabet);
        }

        lruCache.put(key, hashids);

        return hashids;
    }
}
