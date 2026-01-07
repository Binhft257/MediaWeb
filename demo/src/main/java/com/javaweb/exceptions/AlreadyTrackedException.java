package com.javaweb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyTrackedException extends RuntimeException {
    public AlreadyTrackedException(String message) {
        super(message);
    }
}
