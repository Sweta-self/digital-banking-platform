package com.banking.digital_banking_platform.banking.service;

import com.banking.digital_banking_platform.banking.dto.LocationInfo;

public interface IpLocationService {
    LocationInfo getLocation(String ip);
}
