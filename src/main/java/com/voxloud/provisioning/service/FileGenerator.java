package com.voxloud.provisioning.service;

import com.voxloud.provisioning.config.DefaultConfig;
import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.service.model.Provisioning;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.voxloud.provisioning.service.ImportantField.CODECS;
import static com.voxloud.provisioning.service.ImportantField.DOMAIN;
import static com.voxloud.provisioning.service.ImportantField.PASSWORD;
import static com.voxloud.provisioning.service.ImportantField.PORT;
import static com.voxloud.provisioning.service.ImportantField.USERNAME;

@Service
public class FileGenerator {

    private final DefaultConfig defaultConfig;
    private final Map<String, DeviceInterface> deviceInterfaces;

    public FileGenerator(DefaultConfig defaultConfig, List<DeviceInterface> deviceInterfaces) {
        this.defaultConfig = defaultConfig;
        this.deviceInterfaces = deviceInterfaces.stream()
                .collect(Collectors.toMap(DeviceInterface::model, Function.identity()));
    }

    public Provisioning generateConfig(Device device) throws IOException {

        Map<String, Object> currentValue = new HashMap<>();
        currentValue.put(USERNAME, device.getUsername());
        currentValue.put(PASSWORD, device.getPassword());

        DeviceInterface deviceInterface = deviceInterfaces.get(device.getModel().toString());

        Map<String, Object> fragmentData = deviceInterface.readFragmentData(device.getOverrideFragment());
        currentValue.putAll(fragmentData);

        currentValue.compute(DOMAIN, (k,v) -> {
            String tmp = (String) v;
            if (tmp == null || tmp.isEmpty()){
                 return defaultConfig.getDefaultDomain();
            }
            return v;
        });

        currentValue.compute(PORT, (k,v) -> {
            String tmp = (String) v;
            if (tmp == null || tmp.isEmpty()){
                return defaultConfig.getDefaultPort();
            }
            return v;
        });

        currentValue.compute(CODECS, (k,v) -> {
            if (v instanceof Collection){
                List<String> tmp = (List<String>) v;
                if (tmp.isEmpty()){
                    return defaultConfig.getDefaultCodecs();
                }
                return v;
            }
            return defaultConfig.getDefaultCodecs();
        } );

        return new Provisioning(deviceInterface.createFile(currentValue), deviceInterface.contentType());
    }

}
