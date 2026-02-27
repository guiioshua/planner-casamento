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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final StorageService storageService;

    @Transactional(readOnly = true)
    public List<InvitationResponse> findAll() {
        return invitationRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public InvitationResponse createInvitation(CreateInvitationRequest request, MultipartFile coverImage) {
        String finalImageUrl = resolveImageUrl(coverImage, request.getCoverImageUrl());

        Invitation invitation = Invitation.builder()
                .familyName(request.getFamilyName())
                .type(request.getType())
                .coverImageUrl(finalImageUrl)
                .messageBody(request.getMessageBody())
                .categories(request.getCategories() != null ? request.getCategories() : List.of("A"))
                .build();

        if (request.getGuests() != null) {
            for (CreateGuestRequest guestRequest : request.getGuests()) {
                invitation.addGuest(
                        guestRequest.getFullName(),
                        guestRequest.getPhone(),
                        parseStatus(guestRequest.getStatus()),
                        guestRequest.isChild());
            }
        }

        return toResponse(invitationRepository.save(invitation));
    }

    @Transactional(readOnly = true)
    public InvitationResponse getBySlug(String slug) {
        Invitation invitation = invitationRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found for slug: " + slug));
        return toResponse(invitation);
    }

    @Transactional
    public InvitationResponse updateInvitation(UUID id, CreateInvitationRequest request, MultipartFile coverImage) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found: " + id));

        invitation.setFamilyName(request.getFamilyName());
        invitation.setType(request.getType());
        invitation.setMessageBody(request.getMessageBody());

        if (request.getCategories() != null) {
            invitation.setCategories(request.getCategories());
        }

        String resolvedUrl = resolveImageUrl(coverImage, request.getCoverImageUrl());
        if (resolvedUrl != null) {
            invitation.setCoverImageUrl(resolvedUrl);
        }

        // Replace the full guest list on update
        invitation.getGuests().clear();
        if (request.getGuests() != null) {
            for (CreateGuestRequest guestRequest : request.getGuests()) {
                invitation.addGuest(
                        guestRequest.getFullName(),
                        guestRequest.getPhone(),
                        parseStatus(guestRequest.getStatus()),
                        guestRequest.isChild());
            }
        }

        return toResponse(invitationRepository.save(invitation));
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

        return toResponse(invitationRepository.save(invitation));
    }

    @Transactional
    public InvitationResponse addGuestToInvitation(String slug, CreateGuestRequest request) {
        Invitation invitation = invitationRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found for slug: " + slug));

        invitation.addGuest(
                request.getFullName(),
                request.getPhone(),
                parseStatus(request.getStatus()),
                request.isChild());

        return toResponse(invitationRepository.save(invitation));
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Determines the final image URL to persist.
     * An uploaded file takes precedence over a URL string provided in the request
     * body.
     */
    private String resolveImageUrl(MultipartFile uploadedFile, String requestUrl) {
        if (uploadedFile != null && !uploadedFile.isEmpty()) {
            return storageService.store(uploadedFile);
        }
        if (requestUrl != null && !requestUrl.isBlank()) {
            return requestUrl;
        }
        return null;
    }

    /**
     * Parses a raw status string into a {@link GuestStatus}, defaulting to PENDING
     * if the value is null or unrecognised.
     */
    private GuestStatus parseStatus(String raw) {
        if (raw == null)
            return GuestStatus.PENDING;
        try {
            return GuestStatus.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException e) {
            return GuestStatus.PENDING;
        }
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
}
