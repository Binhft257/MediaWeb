package com.javaweb.repository;

import com.javaweb.entity.MediaItemEntity;
import com.javaweb.entity.MediaTypeEntity;
import com.javaweb.model.request.MediaSearchRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MediaItemRepositoryCustom {
    List<MediaItemEntity> getMediasWithCondition(Pageable pageable, MediaSearchRequest request);
}
