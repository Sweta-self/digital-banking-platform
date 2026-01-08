package com.banking.digital_banking_platform.banking.entity;

import com.banking.digital_banking_platform.banking.common.enums.AccountStatus;
import com.banking.digital_banking_platform.banking.common.enums.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name="accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String accountNumber;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;//SAVINGS,CURRENT
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;//ACTIVE,FROZEN

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "account")
    private List<Transaction>transactionList;

}
