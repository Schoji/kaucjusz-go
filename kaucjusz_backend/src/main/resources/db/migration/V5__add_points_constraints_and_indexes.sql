CREATE UNIQUE INDEX uq_point_transactions_scan_receipt
    ON point_transactions (receipt_scan_id)
    WHERE receipt_scan_id IS NOT NULL AND transaction_type = 'SCAN';

ALTER TABLE users
    ADD CONSTRAINT chk_users_current_points_non_negative CHECK (current_points >= 0),
    ADD CONSTRAINT chk_users_lifetime_points_non_negative CHECK (lifetime_points >= 0);

ALTER TABLE point_transactions
    ADD CONSTRAINT chk_point_transactions_amount_non_zero CHECK (amount <> 0);

CREATE INDEX idx_point_transactions_user_created
    ON point_transactions (user_id, created_at DESC, id DESC);

CREATE INDEX idx_point_transactions_user_type_created
    ON point_transactions (user_id, transaction_type, created_at DESC);
