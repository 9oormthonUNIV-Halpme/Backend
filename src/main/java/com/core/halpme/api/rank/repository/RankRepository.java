package com.core.halpme.api.rank.repository;

import com.core.halpme.api.rank.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankRepository extends JpaRepository <Rank, Long> {
    Optional<Rank> findByMemberEmail(String email);
}
