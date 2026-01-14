package com.banking.digital_banking_platform.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDto {

    private String templateCode;
    private String recipient;
    private Map<String,String> placeholders;
}
