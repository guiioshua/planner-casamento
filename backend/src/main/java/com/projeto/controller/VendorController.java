package com.projeto.controller;

import com.projeto.dto.VendorRequest;
import com.projeto.dto.VendorResponse;
import com.projeto.service.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @GetMapping
    public List<VendorResponse> listAll() {
        return vendorService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorResponse create(@Valid @RequestBody VendorRequest request) {
        return vendorService.create(request);
    }

    @PutMapping("/{id}")
    public VendorResponse update(@PathVariable UUID id, @Valid @RequestBody VendorRequest request) {
        return vendorService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        vendorService.delete(id);
    }
}

