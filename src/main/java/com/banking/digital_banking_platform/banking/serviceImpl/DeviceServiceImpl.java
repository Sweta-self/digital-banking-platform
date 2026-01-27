package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.dto.LocationInfo;
import com.banking.digital_banking_platform.banking.entity.UserDevice;
import com.banking.digital_banking_platform.banking.repository.UserDeviceRepository;
import com.banking.digital_banking_platform.banking.service.DeviceService;
import com.banking.digital_banking_platform.banking.service.IpLocationService;
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
    private final IpLocationService ipLocationService;

    @Override
    public UserDevice registerDevice(User user, LoginRequest request, String ip) {


        LocationInfo location = ipLocationService.getLocation(ip);

        return registerDeviceInternal(user, request, ip, location);
    }

    @Transactional
    public UserDevice registerDeviceInternal(User user, LoginRequest request, String ip,LocationInfo location) {
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
            int toDeactivate=(int)(activeCount-MAX_DEVICES+1);
            for(int i=0;i<toDeactivate;i++){
                UserDevice d=devices.get(i);
                d.setActive(false);
                deviceRepository.save(d);
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

        if (location != null) {
            device.setCountry(location.getCountry());
            device.setCity(location.getCity());
        }

            //suspicious check
        boolean suspicious=isSuspiciousLogin(user,ip,location);
        device.setSuspicious(suspicious);

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

    private boolean isSuspiciousLogin(
            User user,
            String ip,
            LocationInfo location
    )
    {
        Optional<UserDevice> lastDeviceOpt =
                deviceRepository.findTopByUserIdOrderByLastLoginDesc(user.getId());

        if (lastDeviceOpt.isEmpty()) {
            return false; // first login
        }
        UserDevice lastDevice = lastDeviceOpt.get();

        if(location !=null
                  && lastDevice.getCountry()!=null
                  && location.getCountry()!=null){
            if(!lastDevice.getCountry().equalsIgnoreCase(location.getCountry())){
                return true;
            }
       }
        //IP SUBNET CHANGE
        if(lastDevice.getIpAddress()!=null && ip!=null){
            if(!sameSubnet(lastDevice.getIpAddress(),ip)){
                return true;
            }
        }
        return false;
    }
    private boolean sameSubnet(String ip1,String ip2){
        try{
            return ip1.substring(0,ip1.lastIndexOf('.'))
                    .equals(ip2.substring(0,ip2.lastIndexOf('.')));
        }catch(Exception e){
            return false;
        }
    }
}
