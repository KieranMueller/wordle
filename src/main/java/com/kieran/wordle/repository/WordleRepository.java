package com.kieran.wordle.repository;

import com.kieran.wordle.entity.Wordle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WordleRepository extends JpaRepository<Wordle, Long> {
    List<Wordle> findByOwnerIdIs(Long id);
    void deleteAllByOwnerId(Long id);
    Wordle findByUuidLink(UUID uuid);
    Wordle deleteByUuidLink(UUID uuid);
}
