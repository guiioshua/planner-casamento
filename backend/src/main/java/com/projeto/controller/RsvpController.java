package com.projeto.controller;

import com.projeto.dto.CreateGuestRequest;
import com.projeto.dto.InvitationResponse;
import com.projeto.dto.RsvpUpdateRequest;
import com.projeto.service.InvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rsvp")
@RequiredArgsConstructor
public class RsvpController {

    private final InvitationService invitationService;

    // Public-facing slug lookup used by the RSVP page (mirrors
    // /invitations/slug/{slug} for admin)
    @GetMapping("/{slug}")
    public InvitationResponse getInvitationForRsvp(@PathVariable("slug") String slug) {
        return invitationService.getBySlug(slug);
    }

    @PostMapping("/{slug}/confirm")
    public InvitationResponse confirmRsvp(@PathVariable("slug") String slug,
            @Valid @RequestBody RsvpUpdateRequest request) {
        return invitationService.updateGuestStatuses(slug, request);
    }

    @PostMapping("/{slug}/guests")
    public InvitationResponse addGuest(@PathVariable("slug") String slug,
            @Valid @RequestBody CreateGuestRequest request) {
        return invitationService.addGuestToInvitation(slug, request);
    }
}
