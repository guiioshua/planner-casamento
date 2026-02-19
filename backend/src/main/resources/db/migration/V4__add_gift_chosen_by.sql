ALTER TABLE gifts
    ADD COLUMN chosen_by_invitation_id UUID REFERENCES invitations(id) ON DELETE SET NULL;
