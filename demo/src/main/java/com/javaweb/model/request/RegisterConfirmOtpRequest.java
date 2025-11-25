package com.javaweb.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterConfirmOtpRequest {

    @NotBlank
    private String otp;
}
