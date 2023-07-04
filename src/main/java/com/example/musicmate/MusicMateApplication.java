package com.example.musicmate;

import com.example.musicmate.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class MusicMateApplication {

	@Autowired
	private EmailSenderService emailSenderService;

	public static void main(String[] args) {
		SpringApplication.run(MusicMateApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void sendEmail() {
		emailSenderService.sendEmail("Aziret5265@gmail.com", "This is subject", "This is body of email");
	}
}
