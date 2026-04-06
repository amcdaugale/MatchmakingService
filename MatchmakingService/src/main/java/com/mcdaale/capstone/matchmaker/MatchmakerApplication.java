package com.mcdaale.capstone.matchmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Where this application is instantiated.
 */
@SpringBootApplication
public class MatchmakerApplication {
	private static final String TAG = MatchmakerApplication.class.getSimpleName();

	public static void main(String[] args) {
		SpringApplication.run(MatchmakerApplication.class, args);
		Log.d(TAG, "Matchmaker application started!");
	}

}
