package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.common.enums.NotificationStatus;
import com.banking.digital_banking_platform.banking.common.enums.NotificationType;

import com.banking.digital_banking_platform.banking.common.util.NotificationDbTemplateBuilder;
import com.banking.digital_banking_platform.banking.dto.NotificationRequestDto;
import com.banking.digital_banking_platform.banking.entity.Notification;
import com.banking.digital_banking_platform.banking.entity.NotificationTemplate;
import com.banking.digital_banking_platform.banking.repository.NotificationRepository;

import com.banking.digital_banking_platform.banking.repository.NotificationTemplateRepository;
import com.banking.digital_banking_platform.banking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
   private final NotificationRepository notificationRepository;
   private final NotificationTemplateRepository notificationTemplateRepository;
   private final NotificationDbTemplateBuilder builder;
    @Override
    @Async
    public void sendNotification(String recipient, NotificationType type, String message, String referenceId) {
        Notification notification=new Notification();
        notification.setRecipient(recipient);
        notification.setType(type);
        notification.setMessage(message);
        notification.setReferenceId(referenceId);
        notification.setNotificationStatus(NotificationStatus.PENDING);

        notificationRepository.save(notification);
        try{
            System.out.println("Sending"+type+"to"+recipient+":"+"message");
            //SUCCESS
            notification.setNotificationStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            notificationRepository.save(notification);
        }
        catch(Exception e){
            //Failed to send
            notification.setNotificationStatus(NotificationStatus.FAILED);
            notificationRepository.save(notification);
        }
    }

    @Override
    public void send(NotificationRequestDto request) {
        NotificationTemplate template= notificationTemplateRepository.
                findByCodeAndActiveTrue(
                        request.getTemplateCode()
                )
                .orElseThrow(()->new RuntimeException("Template not found"));
                String finalTitle=builder.build(
                        template.getTitle(),
                        request.getPlaceholders()
                );
                String finalBody=builder.build(
                        template.getBody(),
                        request.getPlaceholders()
                );
                System.out.println("Sending to:"+request.getRecipient());
                System.out.println("Title"+finalTitle);
                System.out.println("Body"+finalBody);

    }
}