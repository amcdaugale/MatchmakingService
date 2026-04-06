package com.mcdaale.capstone.matchmaker.object;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Object to represent the user of the game.
 * TODO: Add user stats.
 */
@Data
@AllArgsConstructor
public class User {
    /**
     * Id of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //private String username;
    //private String name;
}
