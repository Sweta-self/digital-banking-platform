package com.banking.digital_banking_platform.banking.dto;

import com.banking.digital_banking_platform.banking.common.enums.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountRequestDto {

    @NotNull
    private Long customerId;
    @NotNull
    private AccountType accountType;
}
