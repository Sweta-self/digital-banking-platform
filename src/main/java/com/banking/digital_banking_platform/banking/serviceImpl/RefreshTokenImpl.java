package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.entity.RefreshToken;
import com.banking.digital_banking_platform.banking.repository.RefreshTokenRepository;
import com.banking.digital_banking_platform.banking.service.RefreshTokenService;
import com.banking.digital_banking_platform.security.config.JwtUtil;
import com.banking.digital_banking_platform.security.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    @Value("${app.jwt.refresh-expiration-days}")
    private Long refreshTokenDays;

    @Override
    public RefreshToken createRefreshToken(User user) {

        //one user-> one token
      refreshTokenRepository.deleteByUser(user);

      RefreshToken refreshToken=new RefreshToken();
      refreshToken.setUser(user);
      refreshToken.setToken(UUID.randomUUID().toString());
      refreshToken.setExpiryDate(
              Instant.now().plus(refreshTokenDays, ChronoUnit.DAYS)
      );
 return refreshTokenRepository.save(refreshToken);
    }
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().isBefore(Instant.now())){
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired.Please login again.");
        }
        return token;
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        RefreshToken rtoken=refreshTokenRepository.findByToken(refreshToken).orElseThrow(()->
                new RuntimeException("Refresh token not found"));
        verifyExpiration(rtoken);
        User user=rtoken.getUser();
        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }
}
