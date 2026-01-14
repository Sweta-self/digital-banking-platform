package com.banking.digital_banking_platform.banking.service;


import com.banking.digital_banking_platform.banking.common.enums.NotificationType;
import com.banking.digital_banking_platform.banking.dto.NotificationRequestDto;

public interface NotificationService {
    void sendNotification(String recipient, NotificationType type,String message,String referenceId);
 void send(NotificationRequestDto request);

}
