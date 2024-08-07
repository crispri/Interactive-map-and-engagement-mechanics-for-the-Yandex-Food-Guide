CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP SCHEMA IF EXISTS guide CASCADE;

CREATE SCHEMA IF NOT EXISTS guide;

CREATE TABLE IF NOT EXISTS guide.places (
    id TEXT PRIMARY KEY DEFAULT uuid_generate_v4(),
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    approved BOOLEAN NOT NULL,  
    rating REAL NOT NULL,
    price_lower_bound INTEGER UNIQUE NOT NULL,
    price_upper_bound INTEGER UNIQUE NOT NULL,
    open_time TIME NOT NULL,
    close_time TIME NOT NULL,
    address TEXT NOT NULL,
    tags SMALLINT[],
    is_favorite BOOLEAN NOT NULL,
    CHECK (rating >= 1.0 AND rating <= 5.0),
    CHECK (price_lower_bound > 0 AND price_lower_bound <= price_upper_bound)
);


CREATE TABLE IF NOT EXISTS guide.selections (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    is_public SMALLINT NOT NULL
);


CREATE TABLE IF NOT EXISTS guide.users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS guide.places_selections (
    place_id UUID,
    selection_id UUID
    -- foreign key(place_id) REFERENCES guide.places(id),
    -- foreign key(selection_id) REFERENCES guide.selections(id)
);

CREATE TABLE IF NOT EXISTS guide.visibility (
    selection_id UUID NOT NULL,
    user_id UUID NOT NULL
    -- foreign key(user_id) REFERENCES guide.users(id),
    -- foreign key(selection_id) REFERENCES guide.selections(id)
);