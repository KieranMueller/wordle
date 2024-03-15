package com.kieran.wordle.repository;

import com.kieran.wordle.entity.Wordle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordleRepository extends JpaRepository<Wordle, Long> {
}
