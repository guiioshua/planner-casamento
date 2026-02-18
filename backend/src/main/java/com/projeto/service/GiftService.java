package com.projeto.service;

import com.projeto.dto.GiftRequest;
import com.projeto.dto.GiftResponse;
import com.projeto.model.Gift;
import com.projeto.model.GiftStatus;
import com.projeto.repository.GiftRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GiftService {

    private final GiftRepository giftRepository;

    @Transactional(readOnly = true)
    public List<GiftResponse> findAll() {
        return giftRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GiftResponse> findVisible() {
        return giftRepository.findByVisibleTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public GiftResponse create(GiftRequest request) {
        Gift gift = Gift.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .purchaseLink(request.getPurchaseLink())
                .imageUrl(request.getImageUrl())
                // Use getVisible() because the DTO field is a 'Boolean' wrapper
                .visible(request.getVisible())
                .status(request.getStatus() != null ? request.getStatus() : GiftStatus.AVAILABLE)
                .build();
        return toResponse(giftRepository.save(gift));
    }

    @Transactional
    public GiftResponse update(UUID id, GiftRequest request) {
        Gift gift = giftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gift not found: " + id));

        gift.setName(request.getName());
        gift.setPurchaseLink(request.getPurchaseLink());
        gift.setImageUrl(request.getImageUrl());
        gift.setStatus(request.getStatus());

        // Use getVisible() here too
        gift.setVisible(request.getVisible());

        return toResponse(giftRepository.save(gift));
    }

    @Transactional
    public GiftResponse choose(UUID id) {
        Gift gift = giftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gift not found: " + id));
        if (gift.getStatus() == GiftStatus.CHOSEN) {
            throw new IllegalStateException("Gift already chosen: " + id);
        }
        gift.setStatus(GiftStatus.CHOSEN);
        return toResponse(giftRepository.save(gift));
    }

    @Transactional
    public void delete(UUID id) {
        if (!giftRepository.existsById(id)) {
            throw new EntityNotFoundException("Gift not found: " + id);
        }
        giftRepository.deleteById(id);
    }

    private GiftResponse toResponse(Gift gift) {
        return GiftResponse.builder()
                .id(gift.getId())
                .name(gift.getName())
                .purchaseLink(gift.getPurchaseLink())
                .imageUrl(gift.getImageUrl())
                .status(gift.getStatus())
                // Verify if your Gift Model uses Boolean (getVisible) or boolean (isVisible)
                // Assuming Boolean to match DTO:
                .visible(gift.isVisible())
                .build();
    }
}