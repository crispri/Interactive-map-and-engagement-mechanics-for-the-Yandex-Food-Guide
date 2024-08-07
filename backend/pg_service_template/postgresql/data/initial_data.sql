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

INSERT INTO guide.users(id, name, password) VALUES('f1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'John', '1234');

INSERT INTO guide.places(id, lat, lon, name, description, approved, rating,
                         price_lower_bound, price_upper_bound, open_time, close_time, address, is_favorite, tags)
VALUES('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
       55.736310,
       37.596820,
       'Brasserie Lambic',
       'Brasserie Lambic — это бельгийский ресторан, расположенный недалеко от станции метро Парк культуры Сокольнической линии. В ресторане есть два зала, один из которых предназначен для курения кальяна, а другой — для тех, кто предпочитает курить электронные сигареты.',
       TRUE,
       4.9,
       1500,
       2000,
       '12:00',
       '00:00',
       'Турчанинов пер., 3, стр. 5',
       TRUE,
       ARRAY [1, 2, 3]);

INSERT INTO guide.places(id, lat, lon, name, description, approved, rating,
                         price_lower_bound, price_upper_bound, open_time, close_time, address, is_favorite, tags)
VALUES('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
       55.723900,
       37.588220,
       'Sapiens',
       'Sapiens — это ресторан с уникальной концепцией эволюции питания человека от древнейших времен до современности.',
       TRUE,
       4.9,
       2500,
       5000,
       '09:00',
       '00:00',
       'ул. Льва Толстого, 16',
       FALSE,
       ARRAY [1]);


INSERT INTO guide.places(id, lat, lon, name, description, approved, rating,
                         price_lower_bound, price_upper_bound, open_time, close_time, address, is_favorite)
VALUES(
          'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
          55.733097,
          37.592294,
          'Папа Джонс',
          '«Папа Джонс» — это пиццерия, которая предлагает своим гостям широкий выбор пицц на любой вкус',
          FALSE,
          4.2,
          500,
          500,
          '10:00',
          '3:00',
          'Комсомольский просп., 4',
          TRUE);


INSERT INTO guide.selections(id, name, description, is_public)
VALUES('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Рестораны возсле Яндекса', 'Вкусно', 1)
ON CONFLICT DO NOTHING;

INSERT INTO guide.selections(id, name, description, is_public)
VALUES('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Пиццерии возсле Яндекса', 'Быстро', 1)
ON CONFLICT DO NOTHING;



INSERT INTO guide.places_selections(place_id, selection_id)
VALUES('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11')
ON CONFLICT DO NOTHING;

INSERT INTO guide.places_selections(place_id, selection_id)
VALUES('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11')
ON CONFLICT DO NOTHING;

INSERT INTO guide.places_selections(place_id, selection_id)
VALUES('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11')
ON CONFLICT DO NOTHING;
