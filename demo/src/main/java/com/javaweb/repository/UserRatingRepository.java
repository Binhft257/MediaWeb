package com.javaweb.repository;

import com.javaweb.entity.UserRatingEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRatingRepository extends JpaRepository<UserRatingEntity, Integer> {
    List<UserRatingEntity> findByMediaItemId(Integer mediaItemId);
}
