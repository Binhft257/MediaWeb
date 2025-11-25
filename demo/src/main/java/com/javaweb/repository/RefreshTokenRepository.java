package com.javaweb.repository;

import com.javaweb.entity.RefreshTokenEntity;
import com.javaweb.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {
    Optional<RefreshTokenEntity> findByToken(String token);
    void deleteByUser(UserEntity user);
}
