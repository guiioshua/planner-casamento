package com.projeto.service;

import com.projeto.dto.CreateGuestRequest;
import com.projeto.dto.CreateInvitationRequest;
import com.projeto.dto.GuestDto;
import com.projeto.dto.InvitationResponse;
import com.projeto.dto.RsvpUpdateRequest;
import com.projeto.model.Guest;
import com.projeto.model.GuestStatus;
import com.projeto.model.Invitation;
import com.projeto.repository.InvitationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;

    @Transactional(readOnly = true)
    public List<InvitationResponse> findAll() {
        return invitationRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public InvitationResponse createInvitation(CreateInvitationRequest request, String uploadedImageUrl) {
        String finalImageUrl = uploadedImageUrl;
        if (finalImageUrl == null && request.getCoverImageUrl() != null) {
            finalImageUrl = request.getCoverImageUrl();
        }

        Invitation invitation = Invitation.builder()
                .familyName(request.getFamilyName())
                .type(request.getType())
                .coverImageUrl(finalImageUrl)
                .messageBody(request.getMessageBody())
                .categories(request.getCategories() != null ? request.getCategories() : List.of("A"))
                .build();

        if (request.getGuests() != null) {
            for (CreateGuestRequest guestRequest : request.getGuests()) {
                GuestStatus status = null;
                if (guestRequest.getStatus() != null) {
                    try {
                        status = GuestStatus.valueOf(guestRequest.getStatus().toUpperCase());
                    } catch (IllegalArgumentException ignored) {
                    }
                }
                invitation.addGuest(guestRequest.getFullName(), guestRequest.getPhone(), status,
                        guestRequest.isChild());
            }
        }

        Invitation saved = invitationRepository.save(invitation);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public InvitationResponse getBySlug(String slug) {
        Invitation invitation = invitationRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found for slug: " + slug));
        return toResponse(invitation);
    }

    @Transactional
    public InvitationResponse updateInvitation(UUID id, CreateInvitationRequest request, String uploadedImageUrl) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found: " + id));

        invitation.setFamilyName(request.getFamilyName());
        invitation.setType(request.getType());
        invitation.setMessageBody(request.getMessageBody());

        if (request.getCategories() != null) {
            invitation.setCategories(request.getCategories());
        }

        if (uploadedImageUrl != null) {
            invitation.setCoverImageUrl(uploadedImageUrl);
        } else if (request.getCoverImageUrl() != null && !request.getCoverImageUrl().isBlank()) {
            invitation.setCoverImageUrl(request.getCoverImageUrl());
        }

        // We are REPLACING the guest list here in the update method.
        // A more granular approach might be better, but sticking to existing logic.
        invitation.getGuests().clear();
        if (request.getGuests() != null) {
            for (CreateGuestRequest guestRequest : request.getGuests()) {
                GuestStatus status = null;
                if (guestRequest.getStatus() != null) {
                    try {
                        status = GuestStatus.valueOf(guestRequest.getStatus().toUpperCase());
                    } catch (IllegalArgumentException ignored) {
                    }
                }
                invitation.addGuest(guestRequest.getFullName(), guestRequest.getPhone(), status,
                        guestRequest.isChild());
            }
        }

        Invitation saved = invitationRepository.save(invitation);
        return toResponse(saved);
    }

    @Transactional
    public void deleteInvitation(UUID id) {
        if (!invitationRepository.existsById(id)) {
            throw new EntityNotFoundException("Invitation not found: " + id);
        }
        invitationRepository.deleteById(id);
    }

    @Transactional
    public InvitationResponse updateGuestStatuses(String slug, RsvpUpdateRequest request) {
        Invitation invitation = invitationRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found for slug: " + slug));

        for (Guest guest : invitation.getGuests()) {
            GuestStatus newStatus = request.getStatuses().get(guest.getId());
            if (newStatus != null) {
                guest.setStatus(newStatus);
            }
        }

        Invitation saved = invitationRepository.save(invitation);
        return toResponse(saved);
    }

    private InvitationResponse toResponse(Invitation invitation) {
        List<GuestDto> guestDtos = invitation.getGuests().stream()
                .map(this::toGuestDto)
                .toList();

        return InvitationResponse.builder()
                .id(invitation.getId())
                .familyName(invitation.getFamilyName())
                .type(invitation.getType())
                .slug(invitation.getSlug())
                .coverImageUrl(invitation.getCoverImageUrl())
                .messageBody(invitation.getMessageBody())
                .createdAt(invitation.getCreatedAt())
                .categories(invitation.getCategories())
                .guests(guestDtos)
                .build();
    }

    private GuestDto toGuestDto(Guest guest) {
        return GuestDto.builder()
                .id(guest.getId())
                .fullName(guest.getFullName())
                .phone(guest.getPhone())
                .status(guest.getStatus())
                .isChild(guest.isChild())
                .build();
    }

    @Transactional
    public InvitationResponse addGuestToInvitation(String slug, CreateGuestRequest request) {
        Invitation invitation = invitationRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found for slug: " + slug));

        GuestStatus status = GuestStatus.PENDING;
        if (request.getStatus() != null) {
            try {
                status = GuestStatus.valueOf(request.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Keep default
            }
        }

        invitation.addGuest(request.getFullName(), request.getPhone(), status, request.isChild());
        Invitation saved = invitationRepository.save(invitation);
        return toResponse(saved);
    }
}
