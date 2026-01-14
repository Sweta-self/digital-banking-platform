package com.banking.digital_banking_platform.banking.entity;

import com.banking.digital_banking_platform.banking.common.enums.NotificationStatus;
import com.banking.digital_banking_platform.banking.common.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String recipient;//account number,email or phone

    @Column(length=1000)
    private String message;

    private String referenceId;

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus=NotificationStatus.PENDING;
    private LocalDateTime createdAt=LocalDateTime.now();
    private LocalDateTime sentAt;
}
