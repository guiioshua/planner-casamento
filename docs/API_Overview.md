# Planner Casamento - API Overview

Based on the current backend implementation (Spring Boot Controllers & DTOs).

**Base URL:** `http://localhost:8081/api/v1`

---

## 1. Invitations (Convites)

**Controller:** `InvitationController`
**Base Path:** `/invitations`

### List All Invitations
- **GET** `/invitations`
- **Response:** `List<InvitationResponse>` (JSON)

### Get Invitation by Slug
- **GET** `/invitations/slug/{slug}`
- **Response:** `InvitationResponse` (JSON)

### Create Invitation
- **POST** `/invitations`
- **Content-Type:** `multipart/form-data`
- **Parts:**
  - `data` (JSON): `CreateInvitationRequest` schema
  - `coverImage` (File, Optional): Image file for the cover.
- **Behavior:**
  - If `coverImage` file is provided, it is uploaded and its URL is used.
  - Generates a unique `slug` and `id`.
  - Creates associated `guests`.

### Update Invitation
- **PUT** `/invitations/{id}`
- **Content-Type:** `multipart/form-data`
- **Parts:**
  - `data` (JSON): `CreateInvitationRequest` schema
  - `coverImage` (File, Optional): New image file.
- **Behavior:**
  - Updates invitation details.
  - **Warning:** Currently replaces **ALL** guests (deletes old ones, creates new ones with new IDs).

### Delete Invitation
- **DELETE** `/invitations/{id}`
- **Response:** `204 No Content`

#### Data Structures

**`CreateInvitationRequest` (JSON part):**
```json
{
  "familyName": "Família Souza",
  "type": "STANDARD",        // Enum: STANDARD, GODPARENT
  "messageBody": "Venha celebrar conosco!",
  "coverImageUrl": "http://..." // Optional: Logic prefers uploaded file if present
  "categories": ["A", "B"], // List of categories this invitation belongs to
  "guests": [
    {
      "fullName": "João Souza",
      "phone": "11999999999",
      "status": "PENDING",      // Optional (defaults to PENDING, Enum: PENDING, CONFIRMED, DECLINED)
      "isChild": false          // Boolean — must use key "isChild" (not "child")
    }
  ]
}
```

**`InvitationResponse`:**
```json
{
  "id": "uuid",
  "familyName": "Família Souza",
  "type": "STANDARD",
  "slug": "unique-slug-string",
  "coverImageUrl": "http://localhost:8081/uploads/...",
  "messageBody": "...",
  "createdAt": "2024-01-01T10:00:00Z",
  "categories": ["A", "B"],
  "guests": [
    {
      "id": "uuid",
      "fullName": "João Souza",
      "phone": "...",
      "status": "PENDING",
      "isChild": false
    }
  ]
}
```

---

## 2. RSVP (Public Access)

**Controller:** `RsvpController`
**Base Path:** `/rsvp`

### Get Public Invitation Data
- **GET** `/rsvp/{slug}`
- **Response:** `InvitationResponse` (Same as above)
- **Use Case:** Public landing page for the guest to see their invite.

### Confirm/Decline Presence
- **POST** `/rsvp/{slug}/confirm`
- **Body:** `RsvpUpdateRequest` (JSON)
- **Response:** `InvitationResponse` (Updated)

#### Data Structures

**`RsvpUpdateRequest`:**
```json
{
  "statuses": {
    "guest-uuid-1": "CONFIRMED",
    "guest-uuid-2": "DECLINED"
  }
}
```
*Valid Statuses:* `PENDING`, `CONFIRMED`, `DECLINED`

---

## 3. Gifts (Lista de Presentes)

**Controller:** `GiftController`
**Base Path:** `/gifts`

### List All Gifts (Admin)
- **GET** `/gifts`
- **Response:** `List<GiftResponse>` (JSON)

### List Visible Gifts (Public)
- **GET** `/gifts/visible`
- **Response:** `List<GiftResponse>` (JSON)
- **Behavior:** Returns all gifts where `visible = true`. Includes status (`AVAILABLE` or `CHOSEN`).

### Create Gift
- **POST** `/gifts`
- **Body:** `GiftRequest` (JSON)
- **Response:** `GiftResponse`

### Update Gift
- **PUT** `/gifts/{id}`
- **Body:** `GiftRequest` (JSON)
- **Response:** `GiftResponse`

### Mark as Chosen (Guest Action)
- **PATCH** `/gifts/{id}/choose`
- **Body:** `ChooseGiftRequest` (JSON)
- **Response:** `GiftResponse`
- **Behavior:**
  - Changes status to `CHOSEN`.
  - Links the gift to the invitation family (via `invitationSlug`).
  - Throws error if gift is already chosen.

### Delete Gift
- **DELETE** `/gifts/{id}`
- **Response:** `204 No Content`

#### Data Structures

**`GiftRequest`:**
```json
{
  "name": "Micro-ondas",
  "purchaseLink": "https://amazon...",
  "imageUrl": "https://...",
  "visible": true,       // Optional (defaults to true)
  "category": "A",       // Category for filtering (matches invitation categories)
  "status": "AVAILABLE"  // Optional (defaults to AVAILABLE, Enum: AVAILABLE, CHOSEN)
}
```

**`ChooseGiftRequest`:**
```json
{
  "invitationSlug": "familia-souza-abc123" // Slug of the guest's invitation; links gift to family
}
```

**`GiftResponse`:**
```json
{
  "id": "uuid",
  "name": "Micro-ondas",
  "purchaseLink": "...",
  "imageUrl": "...",
  "status": "AVAILABLE",
  "visible": true,
  "category": "A",
  "chosenByFamilyName": "Família Souza" // Null when status is AVAILABLE
}
```

> **Note:** The `isChild` field in guest DTOs is annotated with `@JsonProperty("isChild")` on the backend to prevent Jackson from stripping the `is` prefix (which would otherwise expose it as `"child"` in JSON).

---

## 4. Vendors (Fornecedores - Admin)

**Controller:** `VendorController`
**Base Path:** `/vendors`

### CRUD Operations
- **GET** `/vendors` → List all
- **POST** `/vendors` → Create
- **PUT** `/vendors/{id}` → Update
- **DELETE** `/vendors/{id}` → Delete

#### Data Structures

**`VendorRequest`:**
```json
{
  "companyName": "Buffet X",
  "serviceCategory": "Buffet",
  "contactName": "Maria",
  "contactPhone": "11988888888",
  "price": 5000.00,
  "amountPaid": 1500.00,
  "notes": "Pagar restante até dia 10"
}
```

**`VendorResponse`:** Same fields as request + `id` (UUID).
