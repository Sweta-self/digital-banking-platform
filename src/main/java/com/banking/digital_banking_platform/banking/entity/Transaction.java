package com.banking.digital_banking_platform.banking.entity;

import com.banking.digital_banking_platform.banking.common.enums.TransactionStatus;
import com.banking.digital_banking_platform.banking.common.enums.TransactionType;
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

    private String senderAccount;
    private String receiverAccount;
    private String accountNumber;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;//DEBIT,CREDIT

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status; //SUCCESS,FAILED

    private BigDecimal closingBalance;

    private LocalDateTime transactionDate;

    @Column(nullable=false,unique=true)
    private String referenceId; //UTR /UUID

    @ManyToOne
    @JoinColumn(name="account_id")
    private Account account;

}
