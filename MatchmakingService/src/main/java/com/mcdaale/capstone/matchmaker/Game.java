package com.mcdaale.capstone.matchmaker;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Game {
    @Id
    private Long id;

}
