package com.voxloud.provisioning.service;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.errors.http.InternalServerErrorException;
import com.voxloud.provisioning.errors.http.NotFoundException;
import com.voxloud.provisioning.repository.DeviceRepository;
import com.voxloud.provisioning.service.model.Provisioning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProvisioningServiceImplTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private FileGenerator fileGenerator;

    @InjectMocks
    private ProvisioningServiceImpl service;

    @Test
    public void getProvisioningFile() throws IOException {

        Device device = mock(Device.class);

        when(deviceRepository.findById("address")).thenReturn(Optional.of(device));

        when(fileGenerator.generateConfig(any(Device.class))).thenReturn(new Provisioning("actual", "type"));

        Provisioning actual = service.getProvisioningFile("address");

        assertEquals("actual", actual.getData());
        assertEquals("type", actual.getContentType());
    }

    @Test
    public void getProvisioningFile_notFoundError() {
        when(deviceRepository.findById("address")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getProvisioningFile("address"));
    }

    @Test
    public void getProvisioningFile_serverError() throws IOException {
        Device device = mock(Device.class);
        when(deviceRepository.findById("address")).thenReturn(Optional.of(device));

        when(fileGenerator.generateConfig(any(Device.class))).thenThrow(IOException.class);

        assertThrows(InternalServerErrorException.class, () -> service.getProvisioningFile("address"));
    }
}
