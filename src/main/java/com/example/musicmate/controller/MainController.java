package com.example.musicmate.controller;


import com.example.musicmate.entity.Song;
import com.example.musicmate.repository.SongRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainController {
    private final SongRepository songRepository;
    @GetMapping("/")
    public String home(Model model) {

        List<Song> songs = songRepository.findAll();
        model.addAttribute("songs", songs);

        return "main";
    }

}
