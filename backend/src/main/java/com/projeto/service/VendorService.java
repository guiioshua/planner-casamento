package com.projeto.service;

import com.projeto.dto.VendorRequest;
import com.projeto.dto.VendorResponse;
import com.projeto.model.Vendor;
import com.projeto.repository.VendorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    @Transactional(readOnly = true)
    public List<VendorResponse> findAll() {
        return vendorRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public VendorResponse create(VendorRequest request) {
        Vendor vendor = Vendor.builder()
                .id(UUID.randomUUID())
                .companyName(request.getCompanyName())
                .serviceCategory(request.getServiceCategory())
                .contactName(request.getContactName())
                .contactPhone(request.getContactPhone())
                .price(request.getPrice())
                .amountPaid(request.getAmountPaid())
                .notes(request.getNotes())
                .build();
        return toResponse(vendorRepository.save(vendor));
    }

    @Transactional
    public VendorResponse update(UUID id, VendorRequest request) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vendor not found: " + id));
        vendor.setCompanyName(request.getCompanyName());
        vendor.setServiceCategory(request.getServiceCategory());
        vendor.setContactName(request.getContactName());
        vendor.setContactPhone(request.getContactPhone());
        vendor.setPrice(request.getPrice());
        vendor.setAmountPaid(request.getAmountPaid());
        vendor.setNotes(request.getNotes());
        return toResponse(vendorRepository.save(vendor));
    }

    @Transactional
    public void delete(UUID id) {
        if (!vendorRepository.existsById(id)) {
            throw new EntityNotFoundException("Vendor not found: " + id);
        }
        vendorRepository.deleteById(id);
    }

    private VendorResponse toResponse(Vendor vendor) {
        return VendorResponse.builder()
                .id(vendor.getId())
                .companyName(vendor.getCompanyName())
                .serviceCategory(vendor.getServiceCategory())
                .contactName(vendor.getContactName())
                .contactPhone(vendor.getContactPhone())
                .price(vendor.getPrice())
                .amountPaid(vendor.getAmountPaid())
                .notes(vendor.getNotes())
                .build();
    }
}

