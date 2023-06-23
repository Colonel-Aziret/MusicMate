package com.example.musicmate.repository;

import com.example.musicmate.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {

}

