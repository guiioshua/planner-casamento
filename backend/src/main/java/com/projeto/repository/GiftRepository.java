package com.projeto.repository;

import com.projeto.model.Gift;
import com.projeto.model.GiftStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GiftRepository extends JpaRepository<Gift, UUID> {

    List<Gift> findByVisibleTrue();

    List<Gift> findByVisibleTrueAndStatus(GiftStatus status);
}