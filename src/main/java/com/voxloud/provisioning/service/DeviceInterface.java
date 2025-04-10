package com.voxloud.provisioning.service;

import java.io.IOException;
import java.util.Map;

public interface DeviceInterface {
    String model();
    Map<String, Object> readFragmentData(String string) throws IOException;
    String createFile(Map<String, Object> data) throws IOException;

}
