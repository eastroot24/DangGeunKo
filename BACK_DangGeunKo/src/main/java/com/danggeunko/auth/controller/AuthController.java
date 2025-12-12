package com.danggeunko.auth.controller;


import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.danggeunko.auth.dto.LoginRequest;
import com.danggeunko.auth.jwt.JwtTokenProvider;
import com.danggeunko.auth.jwt.RefreshTokenStore;
import com.danggeunko.user.dto.User;
import com.danggeunko.user.service.UserService;

@RestController
@RequestMapping("/api-auth")
@CrossOrigin("*")
public class AuthController {

    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenStore refreshTokenStore;
    private final UserService userService;
    
    public AuthController(JwtTokenProvider tokenProvider, RefreshTokenStore refreshTokenStore, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.refreshTokenStore = refreshTokenStore;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    	
    	User user = userService.login(loginRequest);
    	
    	if(user == null) {
    		return ResponseEntity.status(401).body("Invalid credentials");
    	}
    	
//        // ★ 로그인 검증 로직 (실제 서비스에서는 DB 연동 + 비밀번호 암호화 필수!!!!)
//        if (!"ssafy".equals(username) || !"1234".equals(password)) {
//            return ResponseEntity.status(401).body("Invalid credentials");
//        }

        String role = "USER";
        String username = loginRequest.getId();
        int userId = user.getUserId();
        String accessToken = tokenProvider.createAccessToken(userId,username, role);
        String refreshToken = tokenProvider.createRefreshToken(userId, username, role);

        // 서버 쪽에 refresh token 저장 
        Instant expiry = tokenProvider.getExpiry(refreshToken);
        refreshTokenStore.save(refreshToken, username, expiry);

        Map<String, String> body = new HashMap<>();
        body.put("accessToken", accessToken);
        body.put("refreshToken", refreshToken);
        
        return ResponseEntity.ok(body);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam String refreshToken) {

        if (!tokenProvider.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }
        int userId = tokenProvider.getUserId(refreshToken);
        String username = tokenProvider.getUsername(refreshToken);
        String role = tokenProvider.getRole(refreshToken);

        if (!refreshTokenStore.isValid(refreshToken, username)) {
            return ResponseEntity.status(401).body("Refresh token not found or expired");
        }
        
        // 토큰 로테이션: 기존 refresh 제거 후 새 토큰 발급
        refreshTokenStore.delete(refreshToken);

        String newAccessToken = tokenProvider.createAccessToken(userId, username, role);
        String newRefreshToken = tokenProvider.createRefreshToken(userId, username, role);
        Instant newExpiry = tokenProvider.getExpiry(newRefreshToken);
        refreshTokenStore.save(newRefreshToken, username, newExpiry);

        Map<String, String> body = new HashMap<>();
        body.put("accessToken", newAccessToken);
        body.put("refreshToken", newRefreshToken);

        return ResponseEntity.ok(body);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String refreshToken) {
        // 단순히 서버 쪽에 저장된 refresh token 제거
        refreshTokenStore.delete(refreshToken);
        return ResponseEntity.ok("Logged out (refresh token invalidated)");
    }
}