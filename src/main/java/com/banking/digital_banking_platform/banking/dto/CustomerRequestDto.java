package com.banking.digital_banking_platform.banking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerRequestDto {

    @NotBlank
    private String fullName;
    @Email
    private String email;
    @NotBlank
    private String mobile;
}
