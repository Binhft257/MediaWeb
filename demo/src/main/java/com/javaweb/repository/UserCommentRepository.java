package com.javaweb.repository;

import com.javaweb.entity.UserCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCommentRepository extends JpaRepository<UserCommentEntity, Integer> {

    List<UserCommentEntity> findByMediaItem_MediaItemId(Integer mediaItemId);

    Optional<UserCommentEntity> findByCommentIdAndMediaItem_MediaItemId(Integer commentId, Integer mediaItemId);
}
