package com.banking.digital_banking_platform.banking.dto;

import com.banking.digital_banking_platform.banking.common.enums.KycStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class KycUpdateRequestDto {
    @NotNull
    private Long customerId;
    @NotNull
    private KycStatus kycStatus;//APPROVED,REJECTED
}
