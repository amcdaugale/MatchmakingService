package com.mcdaale.capstone.matchmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchmakerApplication {
	private static final String TAG = MatchmakerApplication.class.getSimpleName();

	public static void main(String[] args) {
		SpringApplication.run(MatchmakerApplication.class, args);
		Log.d(TAG, "Matchmaker application started!");
	}

}
