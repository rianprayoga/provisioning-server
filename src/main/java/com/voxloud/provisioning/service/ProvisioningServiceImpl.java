package com.voxloud.provisioning.service;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.errors.http.InternalServerErrorException;
import com.voxloud.provisioning.errors.http.NotFoundException;
import com.voxloud.provisioning.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProvisioningServiceImpl implements ProvisioningService {

    private final DeviceRepository deviceRepository;
    private final FileGenerator fileGenerator;

    public ProvisioningServiceImpl(
            DeviceRepository deviceRepository,
            FileGenerator fileGenerator
        ) {
        this.deviceRepository = deviceRepository;
        this.fileGenerator = fileGenerator;
    }

    public String getProvisioningFile(String macAddress) {

        Device device = deviceRepository
                .findById(macAddress)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Configuration with mac address %s not found.", macAddress)));

        return generateConfig(device);
    }

    private String generateConfig(Device device){
        try{
            return fileGenerator.generateConfig(device);
        }catch (IOException e){
            throw new InternalServerErrorException("An unexpected error occurred.");
        }
    }

}
