package com.mcdaale.capstone.matchmaker;

import com.mcdaale.capstone.matchmaker.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to store match entity.
 * TODO: test if other data can be stored in the same repository, or do they need their own?
 */
@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
}
