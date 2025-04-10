package com.voxloud.provisioning.service;

import com.voxloud.provisioning.entity.Device;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
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

public class DeskDeviceTest {

    private static DeskDevice deskDevice;

    @BeforeClass
    public static void setup() {
        deskDevice = new DeskDevice();
    }

    @Test
    public void readFragment() throws IOException {
        String fragmentString = "domain=sip.anotherdomain.com\nport=5161\ntimeout=10\ncodecs=123";

        Map<String, Object> fragmentData = deskDevice.readFragmentData(fragmentString);

        assertEquals(4, fragmentData.size());
        assertTrue(fragmentData.containsKey(DOMAIN));
        assertEquals(fragmentData.get(DOMAIN),"sip.anotherdomain.com");
        assertTrue(fragmentData.containsKey(PORT));
        assertEquals(fragmentData.get(PORT),"5161");
        assertTrue(fragmentData.containsKey("timeout"));
        assertEquals(fragmentData.get("timeout"),"10");
        assertTrue(fragmentData.containsKey(CODECS));
        assertTrue(fragmentData.get(CODECS) instanceof Collection<?>);
    }

    @Test
    public void readFragmentData_emptyString() throws IOException {
        String fragment = "";
        Map<String, Object> fragmentData = deskDevice.readFragmentData(fragment);

        assertEquals(0, fragmentData.size());
    }

    @Test
    public void readFragmentData_nullString() throws IOException {
        Map<String, Object> fragmentData = deskDevice.readFragmentData(null);

        assertEquals(0, fragmentData.size());
    }

    @Test
    public void createFile() {
        List<String> codecs = new ArrayList<>();
        codecs.add("3");

        Map<String, Object> value = new LinkedHashMap<>();
        value.put(DOMAIN, "1");
        value.put(PORT, "2");
        value.put(CODECS, codecs);

        String actual = deskDevice.createFile(value);
        String expected = "domain=1\nport=2\ncodecs=3";

        assertEquals(expected, actual);
    }

    @Test
    public void contentType(){
        assertEquals("text/plain", deskDevice.contentType());
    }

    @Test
    public void model() {
        assertEquals(Device.DeviceModel.DESK.toString(), deskDevice.model());
    }
}
