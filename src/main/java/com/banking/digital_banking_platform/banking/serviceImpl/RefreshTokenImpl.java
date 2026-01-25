package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.entity.RefreshToken;
import com.banking.digital_banking_platform.banking.repository.RefreshTokenRepository;
import com.banking.digital_banking_platform.banking.service.RefreshTokenService;
import com.banking.digital_banking_platform.security.config.JwtUtil;
import com.banking.digital_banking_platform.security.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
      refreshToken.setRevoked(false);
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

    @Override
    @Transactional
    public String rotateRefreshToken(String oldToken) {
       RefreshToken existingToken=
               refreshTokenRepository.findByToken(oldToken)
                       .orElseThrow(()->new RuntimeException("Invalid refresh Token"));
       //REUSE DETECTION
        if(existingToken.isRevoked()){
            //means already used once
            refreshTokenRepository.deleteByUser(existingToken.getUser());
            throw new RuntimeException("Refresh token reuse detected.Please login again.");
        }
        verifyExpiration(existingToken);
       // mark old token as used
        existingToken.setRevoked(true);
        refreshTokenRepository.save(existingToken);


        //Rotation
        RefreshToken newToken= new RefreshToken();
        newToken.setUser(existingToken.getUser());
        newToken.setToken(UUID.randomUUID().toString());

        //same expiry
        newToken.setExpiryDate(existingToken.getExpiryDate());

        newToken.setRevoked(false);
        refreshTokenRepository.save(newToken);
        return newToken.getToken();
    }

    @Transactional
    @Override
    public void logout(String refreshToken) {

        RefreshToken token=
                refreshTokenRepository.findByToken(refreshToken)
                        .orElseThrow(()->new RuntimeException("Invalid token"));
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    @Transactional
    @Override
    public void logoutAllDevices(Long userId) {
 refreshTokenRepository.revokeAllByUser(userId);
    }
}
