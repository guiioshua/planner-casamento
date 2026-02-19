-- Add category column to gifts
ALTER TABLE gifts ADD COLUMN category VARCHAR(255) NOT NULL DEFAULT 'A';

-- Create invitation_categories table
CREATE TABLE invitation_categories (
    invitation_id UUID NOT NULL,
    category VARCHAR(255) NOT NULL,
    CONSTRAINT fk_invitation_categories_invitation FOREIGN KEY (invitation_id) REFERENCES invitations (id) ON DELETE CASCADE
);

-- Default existing invitations to category 'A'
INSERT INTO invitation_categories (invitation_id, category)
SELECT id, 'A' FROM invitations;
