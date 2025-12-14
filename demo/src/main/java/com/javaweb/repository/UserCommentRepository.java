package com.javaweb.repository;

import com.javaweb.entity.UserCommentEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCommentRepository extends JpaRepository<UserCommentEntity, Integer> {
    List<UserCommentEntity> findByMediaItemId(Integer mediaItemId);
}
