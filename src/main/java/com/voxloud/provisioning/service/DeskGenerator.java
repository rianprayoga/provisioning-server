package com.voxloud.provisioning.service;

import com.voxloud.provisioning.entity.Device;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Component
public class DeskGenerator implements DeviceInterface {

    private final String format = "%s=%s";

    @Override
    public String model() {
        return Device.DeviceModel.DESK.toString();
    }

    @Override
    public Map<String, Object> readFragmentData(String string) throws IOException {
        if (string == null || string.isEmpty()) return Collections.emptyMap();

        Properties properties = new Properties();
        properties.load(new StringReader(string));
        Map<String, Object> map = new HashMap<>();
        properties.forEach((o,o2) -> map.put((String) o, o2));
        return map;
    }

    @Override
    public String createFile(Map<String, Object> data) {
        List<String> values = new ArrayList<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String string = entry.getKey();
            Object o = entry.getValue();
            if (o instanceof Collection) {
                List<String> list = (List<String>) o;
                String concatValue = String.join(",", list);
                values.add(String.format(format, string, concatValue));
                continue;
            }
            values.add(String.format(format, string, o));
        }

        return String.join("\n", values);
    }
}
