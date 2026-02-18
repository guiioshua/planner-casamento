package com.projeto.controller;

import com.projeto.dto.CreateInvitationRequest;
import com.projeto.dto.InvitationResponse;
import com.projeto.service.InvitationService;
import com.projeto.service.StorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;
    private final StorageService storageService;

    @GetMapping
    public List<InvitationResponse> listAll() {
        return invitationService.findAll();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public InvitationResponse create(
            @Valid @RequestPart("data") CreateInvitationRequest request,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {

        String imageUrl = null;
        if (coverImage != null && !coverImage.isEmpty()) {
            imageUrl = storageService.store(coverImage);
        }
        return invitationService.createInvitation(request, imageUrl);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public InvitationResponse update(
            @PathVariable UUID id,
            @Valid @RequestPart("data") CreateInvitationRequest request,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {

        String imageUrl = null;
        if (coverImage != null && !coverImage.isEmpty()) {
            imageUrl = storageService.store(coverImage);
        }
        return invitationService.updateInvitation(id, request, imageUrl);
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