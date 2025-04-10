package com.voxloud.provisioning.service;

import com.voxloud.provisioning.service.model.Provisioning;

public interface ProvisioningService {

    Provisioning getProvisioningFile(String macAddress);
}
