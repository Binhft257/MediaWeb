package com.javaweb.service;

import com.javaweb.model.request.MediaSearchRequest;
import com.javaweb.model.response.MediaSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MediaService {
    Page<MediaSearchResponse> getMedias(Pageable pageable, MediaSearchRequest mediaSearchRequest);
}
