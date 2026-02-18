package com.projeto.controller;

import com.projeto.dto.CreateInvitationRequest;
import com.projeto.dto.InvitationResponse;
import com.projeto.service.InvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @GetMapping
    public List<InvitationResponse> listAll() {
        return invitationService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvitationResponse create(@Valid @RequestBody CreateInvitationRequest request) {
        return invitationService.createInvitation(request);
    }

    @PutMapping("/{id}")
    public InvitationResponse update(@PathVariable UUID id, @Valid @RequestBody CreateInvitationRequest request) {
        return invitationService.updateInvitation(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        invitationService.deleteInvitation(id);
    }

    @GetMapping("/slug/{slug}")
    public InvitationResponse getBySlug(@PathVariable String slug) {
        return invitationService.getBySlug(slug);
    }
}

