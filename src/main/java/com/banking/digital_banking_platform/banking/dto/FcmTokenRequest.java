package com.banking.digital_banking_platform.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcmTokenRequest {
    private Long userId;
    private String token;
    private String platform;
}
