package com.voxloud.provisioning.errors.http;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class NotFoundException extends HttpException {

    public NotFoundException(String message) {
        super(NOT_FOUND, message);
    }

}
