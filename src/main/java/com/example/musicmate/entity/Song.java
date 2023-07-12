package com.example.musicmate.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String artist;
    private String album;
    @Column(columnDefinition = "TEXT")
    private String image;
    private String genre;
    @Column(columnDefinition = "TEXT")
    private String audio;
    private int year;


}

