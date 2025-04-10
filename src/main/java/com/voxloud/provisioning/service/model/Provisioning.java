package com.voxloud.provisioning.service.model;

import lombok.Getter;

@Getter
public class Provisioning {
    private final String data;
    private final String contentType;

    public Provisioning(String data, String contentType) {
        this.data = data;
        this.contentType = contentType;
    }
}
