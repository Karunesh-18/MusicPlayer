package com.example.MusicPlayer;

import org.springframework.boot.SpringApplication;

public class TestMusicPlayerApplication {

	public static void main(String[] args) {
		SpringApplication.from(MusicPlayerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
