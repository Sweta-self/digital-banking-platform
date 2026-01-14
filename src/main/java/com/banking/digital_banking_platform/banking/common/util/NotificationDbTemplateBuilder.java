package com.banking.digital_banking_platform.banking.common.util;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationDbTemplateBuilder {
    public String build(String template, Map<String,String>data){
        String result=template;
        for(Map.Entry<String,String>entry:data.entrySet()){
            result=result.replace(
                    "{{"+entry.getKey()+"}}",
                    entry.getValue()
            );
        }
        return result;
    }
}
