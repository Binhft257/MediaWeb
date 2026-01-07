package com.javaweb.repository;

import com.javaweb.entity.UserMediaLogEntity;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMediaLogRepository extends JpaRepository<UserMediaLogEntity, Integer> {


    boolean existsByUser_IdAndMediaItem_MediaItemId(Integer userId, Integer mediaItemId);

    Optional<UserMediaLogEntity> findByUser_IdAndMediaItem_MediaItemId(Integer userId, Integer mediaItemId);
    Page<UserMediaLogEntity> findByUser_Id(Integer userId, Pageable pageable);

    Page<UserMediaLogEntity> findByUser_IdAndMediaItem_MediaType_TypeName(
            Integer userId, String typeName, Pageable pageable
    );

}
