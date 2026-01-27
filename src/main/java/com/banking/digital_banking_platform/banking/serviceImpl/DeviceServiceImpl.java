package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.entity.UserDevice;
import com.banking.digital_banking_platform.banking.repository.UserDeviceRepository;
import com.banking.digital_banking_platform.banking.service.DeviceService;
import com.banking.digital_banking_platform.security.auth.LoginRequest;
import com.banking.digital_banking_platform.security.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final UserDeviceRepository deviceRepository;
    private static final int MAX_DEVICES=3;

    @Override
    @Transactional
    public UserDevice registerDevice(User user, LoginRequest request, String ip) {
        Optional<UserDevice> optional=
                deviceRepository.findByUserIdAndDeviceId(
                        user.getId(),
                        request.getDeviceId()
                );
        UserDevice device;
        if(optional.isPresent()){

            device=optional.get();
            device.setLastLogin(Instant.now());
            device.setActive(true);
            device.setIpAddress(ip);
            return deviceRepository.save(device);
        }
        long activeCount=deviceRepository.countByUserIdAndActiveTrue(user.getId());
        if(activeCount>=MAX_DEVICES){
            //FIND OLDEST DEVICES
            List<UserDevice> devices=deviceRepository.findByUserIdAndActiveTrueOrderByLastLoginAsc(
                    user.getId()
            );
            if(!devices.isEmpty()){
                UserDevice oldest=devices.get(0);
                oldest.setActive(false);
                deviceRepository.save(oldest);
            }
        }

            device=new UserDevice();
            device.setUser(user);
            device.setDeviceId(request.getDeviceId());
            device.setDeviceName(request.getDeviceName());
            device.setOs(request.getOs());
            device.setBrowser(request.getBrowser());
            device.setIpAddress(ip);
            device.setLastLogin(Instant.now());
            device.setActive(true);

       return deviceRepository.save(device);
    }

    @Transactional
    @Override
    public void deactivateDevice(Long userId, String deviceId) {
        UserDevice device=
                deviceRepository.findByUserIdAndDeviceId(userId, deviceId)
                        .orElseThrow(()->new RuntimeException("Device not found"));
        device.setActive(false);
        deviceRepository.save(device);
    }
}
