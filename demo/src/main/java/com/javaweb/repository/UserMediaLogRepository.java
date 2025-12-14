package com.javaweb.repository;

import com.javaweb.entity.UserMediaLogEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMediaLogRepository extends JpaRepository<UserMediaLogEntity, Integer> {
    List<UserMediaLogEntity> findByUserId(Integer userId);
}
