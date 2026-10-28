CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE users (
    id UUID PRIMARY KEY,
    firebase_uid VARCHAR(128) UNIQUE NOT NULL,
    nickname VARCHAR(50) UNIQUE,
    region VARCHAR(100),
    current_points INTEGER NOT NULL DEFAULT 0,
    lifetime_points INTEGER NOT NULL DEFAULT 0,
    current_streak INTEGER NOT NULL DEFAULT 0,
    last_scan_date DATE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMPTZ
);

CREATE TABLE pets (
    id UUID PRIMARY KEY,
    user_id UUID UNIQUE NOT NULL REFERENCES users(id),
    level INTEGER NOT NULL DEFAULT 1,
    xp INTEGER NOT NULL DEFAULT 0,
    base_hunger INTEGER NOT NULL DEFAULT 100,
    base_mood INTEGER NOT NULL DEFAULT 100,
    last_interaction_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE vending_machines (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    address VARCHAR(255) NOT NULL,
    location GEOGRAPHY(Point, 4326) NOT NULL,
    photo_url VARCHAR(500),
    opening_hours JSONB,
    average_rating NUMERIC(3, 2) NOT NULL DEFAULT 0.00,
    reviews_count INTEGER NOT NULL DEFAULT 0,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    reported_by_user_id UUID REFERENCES users(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE machine_reviews (
    id UUID PRIMARY KEY,
    machine_id UUID NOT NULL REFERENCES vending_machines(id),
    user_id UUID NOT NULL REFERENCES users(id),
    rating SMALLINT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (machine_id, user_id)
);

CREATE TABLE receipt_scans (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    matched_machine_id UUID REFERENCES vending_machines(id),
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    receipt_fingerprint VARCHAR(255) UNIQUE,
    store_name VARCHAR(100),
    deposit_amount NUMERIC(6, 2),
    bottles_count INTEGER NOT NULL DEFAULT 0,
    points_awarded INTEGER NOT NULL DEFAULT 0,
    printed_at TIMESTAMPTZ,
    scan_location GEOGRAPHY(Point, 4326),
    scanned_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE point_transactions (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    receipt_scan_id UUID REFERENCES receipt_scans(id),
    amount INTEGER NOT NULL,
    transaction_type VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);