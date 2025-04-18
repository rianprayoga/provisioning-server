package com.voxloud.provisioning.controller;

import com.voxloud.provisioning.service.ProvisioningService;
import com.voxloud.provisioning.service.model.Provisioning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ProvisioningController {

    private final ProvisioningService service;

    public ProvisioningController(ProvisioningService service) {
        this.service = service;
    }

    @GetMapping(path = "/provisioning/{address}")
    public ResponseEntity<String> getConfiguration(@PathVariable String address) {

        Provisioning provisioningFile = service.getProvisioningFile(address);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", provisioningFile.getContentType());
        return new ResponseEntity<>(provisioningFile.getData(), responseHeaders, HttpStatus.OK);
    }
}