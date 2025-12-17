package com.javaweb.repository;

import com.javaweb.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {

    // Dùng ở nhiều chỗ cũ
    List<GenreEntity> findByGenreNameIn(List<String> genres);

    // Dùng cho logic validate genre mới
    List<GenreEntity> findAllByGenreNameIn(List<String> genres);
}
