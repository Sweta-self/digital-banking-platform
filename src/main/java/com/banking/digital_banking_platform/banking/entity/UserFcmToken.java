package com.banking.digital_banking_platform.banking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user_fcm_token",uniqueConstraints=@UniqueConstraint(columnNames =
        {"user_id","fcm_token"}))
public class UserFcmToken {
    @Id
    @GeneratedValue(strategy  =GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id",nullable = false)
    private Long userId;

    @Column(name="fcm_token",length=500,nullable=false)
    private String fcmToken;

    @Column(name="platform")
    private String platform;//WEB ,ANDROID,IOS

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate(){
        createdAt=LocalDateTime.now();
        updatedAt=createdAt;
    }

    @PreUpdate
    public void onUpdate(){
        updatedAt=LocalDateTime.now();
    }

}
