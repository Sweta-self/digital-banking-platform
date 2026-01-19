package com.banking.digital_banking_platform.security.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;//encoded

    @Enumerated(EnumType.STRING)
    private Role role;//ADMIN,CUSTOMER

    private boolean active=true;
}
