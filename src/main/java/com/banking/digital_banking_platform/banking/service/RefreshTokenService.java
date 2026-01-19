package com.banking.digital_banking_platform.banking.service;

import com.banking.digital_banking_platform.banking.entity.RefreshToken;
import com.banking.digital_banking_platform.security.user.User;

public interface RefreshTokenService {

    public RefreshToken createRefreshToken(User user);
    public RefreshToken verifyExpiration(RefreshToken token);
    public String refreshAccessToken(String token);
}
