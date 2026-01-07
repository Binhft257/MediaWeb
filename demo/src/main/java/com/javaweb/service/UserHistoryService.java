package com.javaweb.service;

import com.javaweb.model.response.UserHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserHistoryService {

    void recordView(Integer userId, Integer mediaItemId);

    Page<UserHistoryResponse> getMyHistory(Integer userId, Pageable pageable);

    void deleteOne(Integer userId, Integer mediaItemId);

    void clearAll(Integer userId);
}
