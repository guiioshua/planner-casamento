package com.projeto.controller;

import com.projeto.dto.ChooseGiftRequest;
import com.projeto.dto.GiftRequest;
import com.projeto.dto.GiftResponse;
import com.projeto.service.GiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/gifts")
@RequiredArgsConstructor
public class GiftController {

    private final GiftService giftService;

    @GetMapping
    public List<GiftResponse> listAll() {
        return giftService.findAll();
    }

    @GetMapping("/visible")
    public List<GiftResponse> listVisible() {
        return giftService.findVisible();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftResponse create(@Valid @RequestBody GiftRequest request) {
        System.out.println("GiftController.create received: " + request);
        return giftService.create(request);
    }

    @PutMapping("/{id}")
    public GiftResponse update(@PathVariable("id") UUID id, @Valid @RequestBody GiftRequest request) {
        System.out.println("GiftController.update received for " + id + ": " + request);
        return giftService.update(id, request);
    }

    @PatchMapping("/{id}/choose")
    public GiftResponse choose(@PathVariable("id") UUID id, @RequestBody(required = false) ChooseGiftRequest request) {
        String slug = request != null ? request.getInvitationSlug() : null;
        return giftService.choose(id, slug);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        giftService.delete(id);
    }
}