package com.banking.digital_banking_platform.banking.scheduler;

import com.banking.digital_banking_platform.banking.common.enums.NotificationStatus;
import com.banking.digital_banking_platform.banking.entity.Notification;
import com.banking.digital_banking_platform.banking.repository.NotificationRepository;
import com.banking.digital_banking_platform.banking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationRetryScheduler {
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    @Scheduled(fixedDelay = 300000)//every 5 minutes
    public void retryFailedNotifications(){
        List<Notification>failedNotifications=notificationRepository.findByNotificationStatus(
                NotificationStatus.FAILED
        );
        for(Notification n:failedNotifications){
            notificationService.sendNotification(n.getRecipient(),
                    n.getType(),n.getMessage(),n.getReferenceId());
        }
    }


}

