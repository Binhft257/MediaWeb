package com.javaweb.repository;

import com.javaweb.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {

    List<GenreEntity> findByGenreNameIn(List<String> genres);

    List<GenreEntity> findAllByGenreNameIn(List<String> genres);
}
