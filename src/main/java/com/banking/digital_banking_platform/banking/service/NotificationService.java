package com.banking.digital_banking_platform.banking.service;


import com.banking.digital_banking_platform.banking.common.enums.NotificationType;

public interface NotificationService {
    void sendNotification(String recipient, NotificationType type,String message,String referenceId);
}
