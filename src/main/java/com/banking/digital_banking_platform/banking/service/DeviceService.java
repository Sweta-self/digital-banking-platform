package com.banking.digital_banking_platform.banking.service;

import com.banking.digital_banking_platform.banking.entity.UserDevice;
import com.banking.digital_banking_platform.security.auth.LoginRequest;
import com.banking.digital_banking_platform.security.user.User;

public interface DeviceService {
    UserDevice registerDevice(User user,
                              LoginRequest request,
                              String ip);
    void deactivateDevice(Long userId, String deviceId);
}
