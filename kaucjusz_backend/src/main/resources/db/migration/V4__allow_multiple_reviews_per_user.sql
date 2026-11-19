ALTER TABLE machine_reviews
    DROP CONSTRAINT IF EXISTS uq_machine_reviews_machine_user;

CREATE INDEX idx_machine_reviews_machine_user_created
    ON machine_reviews (machine_id, user_id, created_at DESC, id DESC);

CREATE INDEX idx_machine_reviews_machine_created
    ON machine_reviews (machine_id, created_at DESC, id DESC);