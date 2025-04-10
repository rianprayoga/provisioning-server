package com.voxloud.provisioning.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class DefaultConfig {

    private final String defaultDomain;
    private final String defaultPort;
    private final List<String> defaultCodecs;

    public DefaultConfig(
            @Value("${provisioning.domain}") String defaultDomain,
            @Value("${provisioning.port}") String defaultPort,
            @Value("${provisioning.codecs}") List<String> defaultCodecs) {

        this.defaultDomain = defaultDomain;
        this.defaultPort = defaultPort;
        this.defaultCodecs = defaultCodecs;
    }
}
