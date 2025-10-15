package com.example.Ex02.repository;

import com.example.Ex02.entity.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<MovieEntity, Integer> {
    Page<MovieEntity> findByNameContainingOrGenreContaining(String keyword, String keyword1, Pageable pageable);
}
