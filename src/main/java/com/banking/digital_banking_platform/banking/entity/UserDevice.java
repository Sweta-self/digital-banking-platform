package com.banking.digital_banking_platform.banking.entity;

import com.banking.digital_banking_platform.security.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name="user_devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDevice {

    @Id
    @GeneratedValue
    private Long id;

    private String deviceId;
    private String deviceName;
    private String os;
    private String browser;
    private String ipAddress;
    private Instant lastLogin;
    private boolean active;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

}
