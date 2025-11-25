package com.javaweb.service;

import com.javaweb.model.request.RegisterConfirmOtpRequest;
import com.javaweb.model.request.RegisterRequest;
import com.javaweb.model.response.ApiResponse;

public interface RegistrationService {
    ApiResponse<?> requestRegister(RegisterRequest req);
    ApiResponse<?> confirmRegister(RegisterConfirmOtpRequest req);
}
