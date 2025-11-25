package com.javaweb.service;

import com.javaweb.model.request.PasswordResetConfirmRequest;
import com.javaweb.model.request.PasswordResetRequest;
import com.javaweb.model.response.ApiResponse;

public interface PasswordResetService {

    ApiResponse<?> requestOtp(PasswordResetRequest req);

    ApiResponse<?> confirmOtp(PasswordResetConfirmRequest req);
}
