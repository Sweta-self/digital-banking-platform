package com.banking.digital_banking_platform.banking.entity;

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
    private String fullName;
    @Column(unique = true)
    private String email;
    private String mobile;
    private String kycStatus; //PENDING,VERIFIED

    @OneToMany(mappedBy = "customer")
    private List<Account> accountList;
}
