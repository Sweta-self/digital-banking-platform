package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.dto.LocationInfo;
import com.banking.digital_banking_platform.banking.service.IpLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IpLocationServiceImpl implements IpLocationService {

    @Override
    public LocationInfo getLocation(String ip) {
        //Dummy
        LocationInfo info =new LocationInfo();
        info.setCountry("India");
        info.setCity("patna");
        return info;
    }
}
