package com.javaweb.repository;

import com.javaweb.entity.UserHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserHistoryRepository extends JpaRepository<UserHistoryEntity, Integer> {

    Optional<UserHistoryEntity> findByUser_IdAndMediaItem_MediaItemId(Integer userId, Integer mediaItemId);

    Page<UserHistoryEntity> findByUser_IdOrderByCreatedAtDesc(Integer userId, Pageable pageable);

    void deleteByUser_IdAndMediaItem_MediaItemId(Integer userId, Integer mediaItemId);

    void deleteByUser_Id(Integer userId);
}
