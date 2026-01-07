package com.javaweb.repository;

import com.javaweb.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<UserEntity> findByStatus(String status, Pageable pageable);

}
