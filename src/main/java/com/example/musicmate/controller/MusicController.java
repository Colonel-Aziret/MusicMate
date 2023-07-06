package com.example.musicmate.controller;

import com.example.musicmate.entity.Song;
import com.example.musicmate.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller
public class MusicController {

    @Autowired
    private SongRepository songRepository;

    @GetMapping("/music")
    public String musicForm(Model model) {
        model.addAttribute("song", new Song());
        return "music-form";
    }

    @PostMapping("/music")
    public String uploadMusic(@RequestParam("file") MultipartFile file, Song song) {
        if (!file.isEmpty()) {
            try {

                byte[] audioBytes = file.getBytes();

                String base64Audio = Base64.getEncoder().encodeToString(audioBytes);


                song.setAudio(base64Audio);

            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        songRepository.save(song);
        return "redirect:/music";
    }
}

