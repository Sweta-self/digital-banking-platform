package com.banking.digital_banking_platform.banking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(
        name="idempotency_keys",
        uniqueConstraints = @UniqueConstraint(
                columnNames = "idempotencyKey"
        )
)
public class IdempotencyRecord {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false,unique=true)
    private String idempotencyKey;
    private String transactionId;
    private String responseMessage;
    private LocalDateTime createdAt;

}
