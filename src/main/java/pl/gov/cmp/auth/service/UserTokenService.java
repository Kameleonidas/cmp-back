package pl.gov.cmp.auth.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;
import pl.gov.cmp.auth.exception.UserTokenNotFoundException;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserTokenService {

    private final LoadingCache<String, String> tokenCache;

    public UserTokenService() {
        tokenCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build(
                new CacheLoader<>() {
                    @Override
                    public String load(String key) {
                        throw new IllegalStateException();
                    }
                }
        );
    }

    public String addTokenToCache(String token) {
        Objects.requireNonNull(token);
        String uuid = UUID.randomUUID().toString();
        tokenCache.put(uuid, token);
        return uuid;
    }

    public String getTokenFromCache(String token) {
        return Optional.ofNullable(tokenCache.getIfPresent(token)).orElseThrow(() -> new UserTokenNotFoundException(token));
    }

}

