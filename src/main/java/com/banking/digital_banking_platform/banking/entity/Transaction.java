package com.banking.digital_banking_platform.banking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionType;//DEBIT,CREDIT
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String referenceId;

    @ManyToOne
    @JoinColumn(name="account_id")
    private Account account;

}
