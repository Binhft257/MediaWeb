package com.javaweb.repository;

import com.javaweb.entity.MediaItemEntity;
import com.javaweb.entity.UserCommentEntity;
import com.javaweb.entity.UserEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCommentRepository extends JpaRepository<UserCommentEntity, Integer> {
    List<UserCommentEntity> findByMediaItem_MediaItemId(Integer mediaItemId);
    boolean existsByUserAndMediaItem(UserEntity user, MediaItemEntity mediaItem);
    Optional<UserCommentEntity> findByUserAndMediaItem(UserEntity user, MediaItemEntity mediaItem);
}