package com.projeto.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "invitations")
public class Invitation {

    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "family_name", nullable = false)
    private String familyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private InvitationType type;

    @Column(name = "slug", nullable = false, unique = true, updatable = false)
    private String slug;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "message_body")
    private String messageBody;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "invitation", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Guest> guests = new ArrayList<>();

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        if (this.slug == null || this.slug.isBlank()) {
            this.slug = UUID.randomUUID().toString();
        }
        if (this.createdAt == null) {
            this.createdAt = OffsetDateTime.now();
        }
        if (this.type == null) {
            this.type = InvitationType.STANDARD;
        }
    }

    public Guest addGuest(String fullName, String phone) {
        Guest guest = Guest.builder()
                .id(UUID.randomUUID())
                .invitation(this)
                .fullName(fullName)
                .phone(phone)
                .status(GuestStatus.PENDING)
                .build();
        this.guests.add(guest);
        return guest;
    }

    public void removeGuest(Guest guest) {
        this.guests.remove(guest);
        guest.setInvitation(null);
    }
}

