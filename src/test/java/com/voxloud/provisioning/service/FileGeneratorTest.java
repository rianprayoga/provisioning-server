package com.voxloud.provisioning.service;


import com.voxloud.provisioning.config.DefaultConfig;
import com.voxloud.provisioning.entity.Device;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.voxloud.provisioning.service.ImportantField.CODECS;
import static com.voxloud.provisioning.service.ImportantField.DOMAIN;
import static com.voxloud.provisioning.service.ImportantField.PASSWORD;
import static com.voxloud.provisioning.service.ImportantField.PORT;
import static com.voxloud.provisioning.service.ImportantField.USERNAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileGeneratorTest {

    @Mock
    private DefaultConfig defaultConfig;
    private FileGenerator fileGenerator;
    private DeviceInterface conferenceDevice;
    private DeviceInterface deskDevice;

    @Captor
    private ArgumentCaptor<Map<String, Object>> captor;


    @Before
    public void setup() throws IOException {
        when(defaultConfig.getDefaultCodecs())
                .thenReturn(Arrays.asList("default"));
        when(defaultConfig.getDefaultPort()).thenReturn("defaultPort");
        when(defaultConfig.getDefaultDomain()).thenReturn("defaultDomain");

        Map<String, Object> fragment = new HashMap<>();
        fragment.put(DOMAIN, "fragment.domain");
        fragment.put("timeout", "10");

        conferenceDevice = mock(DeviceInterface.class);
        when(conferenceDevice.model()).thenReturn(Device.DeviceModel.CONFERENCE.toString());
        when(conferenceDevice.readFragmentData(anyString()))
                .thenReturn(fragment);

        deskDevice = mock(DeviceInterface.class);
        when(deskDevice.model()).thenReturn(Device.DeviceModel.DESK.toString());
        when(deskDevice.readFragmentData(anyString()))
                .thenReturn(Collections.emptyMap());

        List<DeviceInterface> deviceInterfaces = Arrays.asList(conferenceDevice, deskDevice);

        fileGenerator = new FileGenerator(defaultConfig, deviceInterfaces);
    }

    @Test
    public void generateConfig_partialValueFromDefault() throws IOException {

        Device device = mock(Device.class);
        when(device.getUsername()).thenReturn("username");
        when(device.getPassword()).thenReturn("password");
        when(device.getOverrideFragment()).thenReturn("fragment");
        when(device.getModel()).thenReturn(Device.DeviceModel.CONFERENCE);

        fileGenerator.generateConfig(device);

        verify(conferenceDevice).createFile(captor.capture());

        Map<String, Object> actual = captor.getValue();

        assertEquals("username", actual.get(USERNAME));
        assertEquals("password", actual.get(PASSWORD));
        assertEquals("fragment.domain", actual.get(DOMAIN));
        assertEquals("defaultPort", actual.get(PORT));
        assertEquals(Arrays.asList("default"), actual.get(CODECS));
        assertEquals("10", actual.get("timeout"));

    }

    @Test
    public void generateConfig_allValueFromDefault() throws IOException {

        Device device = mock(Device.class);
        when(device.getUsername()).thenReturn("username");
        when(device.getPassword()).thenReturn("password");
        when(device.getOverrideFragment()).thenReturn("fragment");
        when(device.getModel()).thenReturn(Device.DeviceModel.DESK);

        fileGenerator.generateConfig(device);

        verify(deskDevice).createFile(captor.capture());

        Map<String, Object> actual = captor.getValue();

        assertEquals("username", actual.get(USERNAME));
        assertEquals("password", actual.get(PASSWORD));
        assertEquals("defaultDomain", actual.get(DOMAIN));
        assertEquals("defaultPort", actual.get(PORT));
        assertEquals(Arrays.asList("default"), actual.get(CODECS));
        assertNull(actual.get("timeout"));

    }

}
