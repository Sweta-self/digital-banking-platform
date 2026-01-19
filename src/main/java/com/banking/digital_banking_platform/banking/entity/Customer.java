package com.banking.digital_banking_platform.banking.entity;

import com.banking.digital_banking_platform.banking.common.enums.KycStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="customers")
public class Customer {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false,unique = true)
    private Long userId;
    private String fullName;
    @Column(unique = true)
    private String email;
    private String mobile;
    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus; //PENDING,VERIFIED

    @OneToMany(mappedBy = "customer")
    private List<Account> accountList;
}
