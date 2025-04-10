package com.voxloud.provisioning.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.entity.Device;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class ConferenceGenerator implements DeviceInterface {

    private final ObjectMapper objectMapper;

    public ConferenceGenerator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String model() {
        return Device.DeviceModel.CONFERENCE.toString();
    }

    @Override
    public Map<String, Object> readFragmentData(String string) throws JsonProcessingException {
        if (string == null || string.isEmpty()) return Collections.emptyMap();

        return objectMapper.readValue(string, Map.class);
    }

    @Override
    public String createFile(Map<String, Object> data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
}
