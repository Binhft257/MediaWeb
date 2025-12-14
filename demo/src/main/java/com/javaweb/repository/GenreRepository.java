package com.javaweb.repository;

import com.javaweb.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {
    Optional<List<GenreEntity>> findAllByGenreName(List<String> genres);
}
