package com.qeat.domain.card.repository;

import com.qeat.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByUserId(Long userId);
    Optional<Card> findByIdAndUserId(Long id, Long userId);
}