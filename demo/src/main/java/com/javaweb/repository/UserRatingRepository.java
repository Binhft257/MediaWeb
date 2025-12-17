package com.javaweb.repository;

import com.javaweb.entity.MediaItemEntity;
import com.javaweb.entity.UserEntity;
import com.javaweb.entity.UserRatingEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRatingRepository extends JpaRepository<UserRatingEntity, Integer> {
    List<UserRatingEntity> findByMediaItem_MediaItemId(Integer mediaItemId);
    boolean existsByUserAndMediaItem(UserEntity user, MediaItemEntity mediaItem);
    Optional<UserRatingEntity> findByUserAndMediaItem(UserEntity user, MediaItemEntity mediaItem);
}
