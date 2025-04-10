package com.voxloud.provisioning.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.entity.Device;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.voxloud.provisioning.service.ImportantField.CODECS;
import static com.voxloud.provisioning.service.ImportantField.DOMAIN;
import static com.voxloud.provisioning.service.ImportantField.PORT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConferenceDeviceTest {

    private static ConferenceDevice conferenceDevice;

    @BeforeClass
    public static void setup() {
        ObjectMapper mapper = new ObjectMapper();
        conferenceDevice = new ConferenceDevice(mapper);
    }

    @Test
    public void model() {
        assertEquals(Device.DeviceModel.CONFERENCE.toString(), conferenceDevice.model());
    }

    @Test
    public void readFragmentData() throws JsonProcessingException {
        String fragment = "{" + "\"domain\":\"sip.anotherdomain.com\","
                + "\"port\":\"5161\","
                + "\"timeout\":10,"
                + "\"codecs\": [\"G729\",\"OPUS\"]"
                + "}";

        Map<String, Object> fragmentData = conferenceDevice.readFragmentData(fragment);

        assertEquals(4, fragmentData.size());
        assertTrue(fragmentData.containsKey(DOMAIN));
        assertTrue(fragmentData.containsKey(PORT));
        assertTrue(fragmentData.containsKey("timeout"));
        assertTrue(fragmentData.containsKey(CODECS));

        assertTrue(fragmentData.get(CODECS) instanceof Collection<?>);
    }

    @Test
    public void readFragmentData_emptyString() throws JsonProcessingException {
        String fragment = "";
        Map<String, Object> fragmentData = conferenceDevice.readFragmentData(fragment);

        assertEquals(0, fragmentData.size());
    }

    @Test
    public void readFragmentData_nullString() throws JsonProcessingException {
        String fragment = null;
        Map<String, Object> fragmentData = conferenceDevice.readFragmentData(fragment);

        assertEquals(0, fragmentData.size());
    }

    @Test
    public void createFile() throws JsonProcessingException {
        List<String> codecs = new ArrayList<>();
        codecs.add("3");
        codecs.add("4");

        Map<String, Object> value = new LinkedHashMap<>();
        value.put(DOMAIN, "1");
        value.put(PORT, "2");
        value.put(CODECS, codecs);

        String expected = "{\"domain\":\"1\",\"port\":\"2\",\"codecs\":[\"3\",\"4\"]}";

        String actual = conferenceDevice.createFile(value);

        assertEquals(expected, actual);
    }

    @Test
    public void contentType(){
        assertEquals("application/json", conferenceDevice.contentType());
    }
}
