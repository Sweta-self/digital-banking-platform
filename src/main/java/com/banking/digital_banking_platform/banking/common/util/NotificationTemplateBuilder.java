package com.banking.digital_banking_platform.banking.common.util;

import com.banking.digital_banking_platform.banking.common.enums.NotificationEvent;
import com.banking.digital_banking_platform.banking.dto.NotificationData;
import org.springframework.stereotype.Component;

@Component
public class NotificationTemplateBuilder {
    public String buildMessage(NotificationEvent event, NotificationData data){
        switch(event){

            case FUND_TRANSFER_SUCCESS :
                return "Rs"+data.getAmount()
                        +"transferred successfully to account"
                        +data.getToAccount();

            case FUND_TRANSFER_FAILED:
                return "Fund transfer of Rs"+data.getAmount()
                        +"failed.Ampount will be reversed.";

            case REFUND_PROCESSED:
                return "Rs"+data.getAmount()
                        +"refunded to your account due to failed transfer.";

            case KYC_APPROVED:
                return "Your KYC has been approved.You can now use banking services.";

            case KYC_REJECTED:
                return "Your KYC has been rejected.Please resubmit documents";

            default:
                throw new IllegalArgumentException("Unknown" +
                        "notification event");

        }
    }
}
