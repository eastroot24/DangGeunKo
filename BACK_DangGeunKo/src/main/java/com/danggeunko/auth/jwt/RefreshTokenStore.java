package com.danggeunko.auth.jwt;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 아주 단순한 인메모리 Refresh Token 저장소
 */
@Component
public class RefreshTokenStore {

    public static class RefreshTokenInfo {
        private final String username;
        private final Instant expiry;

        public RefreshTokenInfo(String username, Instant expiry) {
            this.username = username;
            this.expiry = expiry;
        }

        public String getUsername() {
            return username;
        }

        public Instant getExpiry() {
            return expiry;
        }
    }

    private final Map<String, RefreshTokenInfo> store = new ConcurrentHashMap<>();

    public void save(String refreshToken, String username, Instant expiry) {
        store.put(refreshToken, new RefreshTokenInfo(username, expiry));
    }

    public void delete(String refreshToken) {
        store.remove(refreshToken);
    }

    public boolean isValid(String refreshToken, String username) {
        RefreshTokenInfo info = store.get(refreshToken);
        if (info == null) {
            return false;
        }
        if (!info.getUsername().equals(username)) {
            return false;
        }
        if (info.getExpiry().isBefore(Instant.now())) {
            // 만료된 토큰은 제거
            store.remove(refreshToken);
            return false;
        }
        return true;
    }
}
