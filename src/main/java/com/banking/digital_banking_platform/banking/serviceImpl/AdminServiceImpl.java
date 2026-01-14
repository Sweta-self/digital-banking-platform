package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.common.enums.KycStatus;
import com.banking.digital_banking_platform.banking.common.enums.NotificationEvent;
import com.banking.digital_banking_platform.banking.common.enums.NotificationStatus;
import com.banking.digital_banking_platform.banking.common.enums.NotificationType;
import com.banking.digital_banking_platform.banking.common.util.NotificationTemplateBuilder;
import com.banking.digital_banking_platform.banking.dto.KycUpdateRequestDto;
import com.banking.digital_banking_platform.banking.dto.NotificationData;
import com.banking.digital_banking_platform.banking.entity.Customer;
import com.banking.digital_banking_platform.banking.repository.CustomerRepository;
import com.banking.digital_banking_platform.banking.service.AdminService;
import com.banking.digital_banking_platform.banking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final CustomerRepository customerRepository;
    private final NotificationService notificationService;
    NotificationTemplateBuilder templateBuilder;
    Map<KycStatus, NotificationEvent>map=
            Map.of(
                    KycStatus.APPROVED,NotificationEvent.KYC_APPROVED,
                    KycStatus.REJECTED,NotificationEvent.KYC_REJECTED
            );
    @Override
    @Transactional
    public void updateKycStatus(KycUpdateRequestDto request) {
        Customer customer=customerRepository.findById(request.getCustomerId())
                .orElseThrow(()-> new RuntimeException("Customer not found"));
        customer.setKycStatus(request.getKycStatus());
        customerRepository.save(customer);

        String message= templateBuilder.buildMessage(
                map.get(request.getKycStatus()),
                null
        );
        notificationService.sendNotification(
                null,
                NotificationType.PUSH,
                message, null
        );
    }

    @Override
    public List<Customer> getPendingKycCustomers() {
       return customerRepository.findByKycStatus(KycStatus.PENDING);
    }
}
