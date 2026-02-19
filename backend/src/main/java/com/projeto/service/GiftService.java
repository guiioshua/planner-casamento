package com.projeto.service;

import com.projeto.dto.GiftRequest;
import com.projeto.dto.GiftResponse;
import com.projeto.model.Gift;
import com.projeto.model.GiftStatus;
import com.projeto.model.Invitation;
import com.projeto.repository.GiftRepository;
import com.projeto.repository.InvitationRepository;
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
    private final InvitationRepository invitationRepository;

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
                .visible(request.getVisible() != null ? request.getVisible() : true)
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
        if (request.getStatus() != null) {
            gift.setStatus(request.getStatus());
        }

        if (request.getVisible() != null) {
            gift.setVisible(request.getVisible());
        }

        return toResponse(giftRepository.save(gift));
    }

    @Transactional
    public GiftResponse choose(UUID id, String invitationSlug) {
        Gift gift = giftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gift not found: " + id));
        if (gift.getStatus() == GiftStatus.CHOSEN) {
            throw new IllegalStateException("Gift already chosen: " + id);
        }

        Invitation invitation = null;
        if (invitationSlug != null && !invitationSlug.isBlank()) {
            invitation = invitationRepository.findBySlug(invitationSlug)
                    .orElse(null); // Attribution is best-effort; don't block if slug is bad
        }

        gift.setStatus(GiftStatus.CHOSEN);
        gift.setChosenByInvitation(invitation);
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
        String chosenByFamilyName = gift.getChosenByInvitation() != null
                ? gift.getChosenByInvitation().getFamilyName()
                : null;

        return GiftResponse.builder()
                .id(gift.getId())
                .name(gift.getName())
                .purchaseLink(gift.getPurchaseLink())
                .imageUrl(gift.getImageUrl())
                .status(gift.getStatus())
                .visible(gift.isVisible())
                .chosenByFamilyName(chosenByFamilyName)
                .build();
    }
}