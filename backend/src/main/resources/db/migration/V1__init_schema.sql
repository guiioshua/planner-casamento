CREATE TABLE invitations (
    id UUID PRIMARY KEY,
    family_name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    cover_image_url VARCHAR(500),
    message_body TEXT,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE guests (
    id UUID PRIMARY KEY,
    invitation_id UUID NOT NULL REFERENCES invitations (id) ON DELETE CASCADE,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    status VARCHAR(50) NOT NULL
);

CREATE TABLE gifts (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    purchase_link VARCHAR(500),
    image_url VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE vendors (
    id UUID PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    service_category VARCHAR(100) NOT NULL,
    contact_name VARCHAR(255),
    contact_phone VARCHAR(50),
    price NUMERIC(15, 2),
    notes TEXT
);

