package com.javaweb.repository;

import com.javaweb.entity.MediaTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaTypeRepository extends JpaRepository<MediaTypeEntity, Integer> {
    Optional<MediaTypeEntity> findByTypeName(String typeName);
}
