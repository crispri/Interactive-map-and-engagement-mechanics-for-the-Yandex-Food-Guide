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


INSERT INTO guide.places (id, lat, lon, name, description, approved, rating, price_lower_bound, price_upper_bound, open_time, close_time, address, tags, is_favorite) VALUES
    (uuid_generate_v4(), 55.751244, 37.618423, 'Pushkin Cafe', 'One of Moscow’s most famous cafes, known for its exquisite pastries and coffee.', TRUE, 4.5, 1500, 5000, '08:00:00', '22:00:00', 'Tverskoy Blvd, 26А', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.754999, 37.620437, 'White Rabbit', 'A high-end restaurant with stunning views and contemporary Russian cuisine.', TRUE, 4.7, 5000, 20000, '12:00:00', '23:00:00', 'Smolenskaya Square, 3', ARRAY[1, 9], TRUE),
    (uuid_generate_v4(), 55.759303, 37.617536, 'Mari Vanna', 'A cozy restaurant offering traditional Russian dishes in a home-like setting.', TRUE, 4.4, 2000, 8000, '11:00:00', '23:00:00', 'Tverskaya St, 18', ARRAY[2, 7], TRUE),
    (uuid_generate_v4(), 55.749229, 37.606104, 'Dr. Zhivago', 'Classic Russian cuisine in a stylish, retro setting.', TRUE, 4.3, 1800, 9000, '10:00:00', '22:00:00', 'Strastnoy Blvd, 8', ARRAY[1, 6], TRUE),
    (uuid_generate_v4(), 55.751774, 37.616381, 'Beluga', 'Upscale restaurant known for its caviar and seafood dishes.', TRUE, 4.6, 2500, 15000, '12:00:00', '23:00:00', 'Neglinnaya St, 4', ARRAY[1, 10], TRUE),
    (uuid_generate_v4(), 55.757685, 37.621327, 'The Ribs', 'Casual spot famous for its ribs and American BBQ.', TRUE, 4.2, 1200, 6000, '11:00:00', '23:00:00', 'Tverskaya St, 25', ARRAY[2, 8], TRUE),
    (uuid_generate_v4(), 55.754569, 37.621639, 'Café Pushkin', 'Renowned café offering a range of classic Russian and European dishes.', TRUE, 4.5, 1500, 8000, '09:00:00', '22:00:00', 'Tverskoy Blvd, 26', ARRAY[1, 7], TRUE),
    (uuid_generate_v4(), 55.759191, 37.616152, 'Varenichnaya №1', 'Popular for its dumplings and traditional Russian comfort food.', TRUE, 4.3, 1000, 5000, '10:00:00', '23:00:00', 'Arbat St, 1', ARRAY[2, 6], TRUE),
    (uuid_generate_v4(), 55.758167, 37.621137, 'LavkaLavka', 'Farm-to-table restaurant focused on organic and locally sourced ingredients.', TRUE, 4.4, 1500, 7000, '10:00:00', '22:00:00', 'Svetlanskaya St, 14', ARRAY[3, 9], TRUE),
    (uuid_generate_v4(), 55.750794, 37.618121, 'Moscow Pizza', 'A casual spot for pizza and Italian cuisine.', TRUE, 4.1, 1000, 5000, '11:00:00', '23:00:00', 'Tverskoy Blvd, 24', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.751263, 37.616226, 'Gorynych', 'Restaurant offering traditional Russian fare with a cozy ambiance.', TRUE, 4.4, 1400, 7000, '12:00:00', '22:00:00', 'Arbat St, 21', ARRAY[2, 6], TRUE),
    (uuid_generate_v4(), 55.757984, 37.621834, 'Café Sova', 'Modern café known for its coffee and light bites.', TRUE, 4.2, 800, 3000, '08:00:00', '20:00:00', 'Chistoprudny Blvd, 2', ARRAY[3, 7], TRUE),
    (uuid_generate_v4(), 55.759410, 37.619098, 'Stroganoff Steakhouse', 'High-end steakhouse offering premium cuts and fine dining experience.', TRUE, 4.5, 2000, 12000, '12:00:00', '23:00:00', 'Tverskaya St, 10', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.755415, 37.617592, 'Lavka', 'An organic restaurant with a focus on healthy, farm-fresh dishes.', TRUE, 4.3, 1600, 9000, '11:00:00', '22:00:00', 'Kudrinskaya Sq, 8', ARRAY[2, 6], TRUE),
    (uuid_generate_v4(), 55.752755, 37.617531, 'Cherdak', 'Rustic café with a focus on homemade food and traditional recipes.', TRUE, 4.4, 1200, 6000, '09:00:00', '21:00:00', 'Chistoprudny Blvd, 12', ARRAY[1, 7], TRUE),
    (uuid_generate_v4(), 55.754838, 37.620007, 'O2 Lounge', 'A stylish rooftop bar offering panoramic views of Moscow.', TRUE, 4.6, 3000, 20000, '18:00:00', '02:00:00', 'Okhotny Ryad, 2', ARRAY[2, 10], TRUE),
    (uuid_generate_v4(), 55.757251, 37.616358, 'Craft Kitchen', 'Modern eatery specializing in craft beer and gourmet burgers.', TRUE, 4.5, 1800, 10000, '12:00:00', '23:00:00', 'Strastnoy Blvd, 6', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.751826, 37.617832, 'The Hoxton', 'A trendy restaurant offering contemporary dishes and stylish decor.', TRUE, 4.3, 1500, 8000, '11:00:00', '23:00:00', 'Tverskaya St, 22', ARRAY[2, 9], TRUE),
    (uuid_generate_v4(), 55.750119, 37.618568, 'Gostinny Dvor', 'A historic restaurant serving traditional Russian dishes.', TRUE, 4.6, 2000, 12000, '12:00:00', '22:00:00', 'Tverskoy Blvd, 18', ARRAY[3, 10], TRUE),
    (uuid_generate_v4(), 55.755972, 37.620188, 'Burger King', 'International fast-food chain known for its burgers and fries.', TRUE, 4.2, 600, 2500, '10:00:00', '22:00:00', 'Tverskaya St, 5', ARRAY[1, 6], TRUE),
    (uuid_generate_v4(), 55.757238, 37.618741, 'Chayhona №1', 'Popular chain offering traditional Central Asian cuisine.', TRUE, 4.3, 1200, 6000, '10:00:00', '22:00:00', 'Arbat St, 7', ARRAY[2, 8], TRUE),
    (uuid_generate_v4(), 55.754512, 37.617654, 'Bar & Grill', 'Relaxed venue for grilled meats and craft beers.', TRUE, 4.4, 1400, 7000, '12:00:00', '23:00:00', 'Kudrinskaya Sq, 6', ARRAY[3, 9], TRUE),
    (uuid_generate_v4(), 55.755433, 37.619882, 'TGI Fridays', 'American restaurant chain with a casual atmosphere and extensive menu.', TRUE, 4.2, 1500, 8000, '11:00:00', '23:00:00', 'Pushkinskaya St, 3', ARRAY[1, 7], TRUE),
    (uuid_generate_v4(), 55.758790, 37.620919, 'Gastronomica', 'Gourmet restaurant focusing on innovative cuisine and elegant dining.', TRUE, 4.6, 2000, 13000, '11:00:00', '22:00:00', 'Svetlanskaya St, 5', ARRAY[2, 10], TRUE),
    (uuid_generate_v4(), 55.752512, 37.619546, 'Art-Café', 'Cafe offering a creative menu and artistic atmosphere.', TRUE, 4.3, 1000, 5000, '10:00:00', '22:00:00', 'Strastnoy Blvd, 11', ARRAY[1, 6], TRUE),
    (uuid_generate_v4(), 55.753388, 37.616854, 'Grill House', 'Casual spot known for its barbecue and steak options.', TRUE, 4.4, 1600, 9000, '12:00:00', '23:00:00', 'Arbat St, 12', ARRAY[3, 8], TRUE),
    (uuid_generate_v4(), 55.755610, 37.621540, 'Italiano', 'Restaurant serving classic Italian dishes with a contemporary twist.', TRUE, 4.5, 1800, 8000, '12:00:00', '22:00:00', 'Tverskaya St, 4', ARRAY[1, 9], TRUE),
    (uuid_generate_v4(), 55.759556, 37.619510, 'Café Noire', 'Upscale café known for its coffee and refined desserts.', TRUE, 4.6, 1500, 7000, '09:00:00', '20:00:00', 'Svetlanskaya St, 8', ARRAY[2, 10], TRUE),
    (uuid_generate_v4(), 55.754773, 37.620932, 'Royal Bar', 'Bar offering a selection of fine spirits and sophisticated cocktails.', TRUE, 4.4, 2000, 10000, '16:00:00', '02:00:00', 'Tverskaya St, 20', ARRAY[3, 6], TRUE),
    (uuid_generate_v4(), 55.757901, 37.621051, 'Kafe Garmonia', 'Cafe with a focus on healthy eating and relaxed atmosphere.', TRUE, 4.3, 800, 3500, '08:00:00', '19:00:00', 'Chistoprudny Blvd, 9', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.755844, 37.619275, 'The Kitchen', 'Casual eatery with a diverse menu and laid-back vibe.', TRUE, 4.2, 1200, 6000, '10:00:00', '22:00:00', 'Arbat St, 6', ARRAY[2, 7], TRUE),
    (uuid_generate_v4(), 55.746594, 37.620920, 'La Maison', 'French restaurant offering a variety of gourmet dishes in a chic setting.', TRUE, 4.5, 2500, 12000, '12:00:00', '23:00:00', 'Kutuzovsky Ave, 14', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.765335, 37.629845, 'Moscow Momo', 'Restaurant specializing in traditional Tibetan dumplings with a modern twist.', TRUE, 4.3, 1000, 5000, '10:00:00', '22:00:00', 'Bagrationovsky Ave, 16', ARRAY[3, 9], TRUE),
    (uuid_generate_v4(), 55.740350, 37.580209, 'Orangery', 'Elegant venue with a diverse menu focusing on international cuisine.', TRUE, 4.6, 2000, 12000, '12:00:00', '23:00:00', 'Prospekt Vernadskogo, 3', ARRAY[1, 7], TRUE),
    (uuid_generate_v4(), 55.727409, 37.583264, 'The Garden', 'Charming café known for its fresh juices and light snacks.', TRUE, 4.2, 800, 4000, '08:00:00', '20:00:00', 'Luzhniki St, 9', ARRAY[2, 6], TRUE),
    (uuid_generate_v4(), 55.790000, 37.640000, 'Sky Lounge', 'High-altitude bar offering stunning views of Moscow with a relaxed atmosphere.', TRUE, 4.7, 3000, 15000, '18:00:00', '02:00:00', 'Vostochnoe Chertanovo, 17', ARRAY[3, 10], TRUE),
    (uuid_generate_v4(), 55.711256, 37.612658, 'Zen Café', 'Trendy café with a focus on Japanese cuisine and specialty coffee.', TRUE, 4.4, 1200, 6000, '10:00:00', '21:00:00', 'Tsaritsyno St, 11', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.723145, 37.607783, 'Peking Duck', 'Authentic Chinese restaurant known for its Peking Duck and dim sum.', TRUE, 4.3, 1500, 8000, '11:00:00', '23:00:00', 'Tsaritsyno St, 14', ARRAY[2, 6], TRUE),
    (uuid_generate_v4(), 55.788922, 37.636817, 'Bistro 54', 'Casual bistro with a variety of dishes from European to Asian cuisine.', TRUE, 4.5, 1800, 7000, '11:00:00', '22:00:00', 'Izmailovo St, 7', ARRAY[3, 9], TRUE),
    (uuid_generate_v4(), 55.702391, 37.601164, 'Craft Beer House', 'Specialty bar offering a range of craft beers and pub food.', TRUE, 4.4, 1400, 6000, '12:00:00', '23:00:00', 'Sirius St, 4', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.752817, 37.614787, 'Café Bella', 'Italian café offering pizzas and pastas in a cozy setting.', TRUE, 4.2, 1200, 5000, '10:00:00', '22:00:00', 'Gorky St, 15', ARRAY[2, 7], TRUE),
    (uuid_generate_v4(), 55.742833, 37.584598, 'Veranda', 'Elegant restaurant with an emphasis on seafood and fine dining.', TRUE, 4.6, 2500, 12000, '12:00:00', '22:00:00', 'Yuzhnaya Ave, 23', ARRAY[1, 9], TRUE),
    (uuid_generate_v4(), 55.738390, 37.622749, 'Moscow Grill', 'Grill house offering a range of barbecue dishes and hearty meals.', TRUE, 4.3, 1600, 7000, '11:00:00', '22:00:00', 'Kotelniki St, 8', ARRAY[2, 6], TRUE),
    (uuid_generate_v4(), 55.755665, 37.621314, 'Tea Room', 'Classic tea house with a wide selection of teas and pastries.', TRUE, 4.2, 1000, 4000, '09:00:00', '20:00:00', 'Kropotkinsky St, 6', ARRAY[3, 8], TRUE),
    (uuid_generate_v4(), 55.752622, 37.624509, 'Gourmet Burger', 'Burgers and sides with gourmet flair and a relaxed atmosphere.', TRUE, 4.4, 1300, 6000, '11:00:00', '23:00:00', 'Patriarch Ponds, 7', ARRAY[1, 9], TRUE),
    (uuid_generate_v4(), 55.738952, 37.594242, 'Russian Feast', 'Restaurant offering a full range of traditional Russian dishes.', TRUE, 4.5, 1800, 9000, '12:00:00', '22:00:00', 'Moscow Center, 20', ARRAY[2, 7], TRUE),
    (uuid_generate_v4(), 55.721458, 37.614823, 'Sushi House', 'Popular sushi restaurant with a modern menu and stylish decor.', TRUE, 4.6, 1500, 8000, '11:00:00', '23:00:00', 'Kolomenskoe St, 12', ARRAY[3, 10], TRUE),
    (uuid_generate_v4(), 55.741203, 37.620128, 'Dinner Club', 'Chic venue offering a diverse menu with an emphasis on fine dining.', TRUE, 4.3, 2000, 10000, '12:00:00', '23:00:00', 'Vernadsky Ave, 8', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.747829, 37.609751, 'Street Food', 'Casual eatery with a range of street food options from around the world.', TRUE, 4.4, 1000, 4000, '11:00:00', '22:00:00', 'Sokolniki St, 5', ARRAY[2, 6], TRUE),
    (uuid_generate_v4(), 55.726171, 37.600349, 'Vino', 'Wine bar offering a selection of fine wines and gourmet small plates.', TRUE, 4.5, 1800, 7000, '12:00:00', '22:00:00', 'Marina Grove, 8', ARRAY[3, 7], TRUE),
    (uuid_generate_v4(), 55.741275, 37.609184, 'Panorama', 'Restaurant with panoramic views and a menu focused on modern European cuisine.', TRUE, 4.6, 2000, 15000, '12:00:00', '23:00:00', 'Prospekt Mira, 18', ARRAY[1, 9], TRUE),
    (uuid_generate_v4(), 55.746517, 37.609943, 'The Tasty Corner', 'A casual dining spot with an emphasis on comfort food and friendly service.', TRUE, 4.3, 1200, 5000, '11:00:00', '22:00:00', 'Luzhniki St, 5', ARRAY[2, 6], TRUE),
    (uuid_generate_v4(), 55.760123, 37.611498, 'Rustic Table', 'Restaurant known for its hearty meals and rustic decor.', TRUE, 4.4, 1400, 6000, '11:00:00', '22:00:00', 'Serebryany Bor, 2', ARRAY[3, 8], TRUE),
    (uuid_generate_v4(), 55.727059, 37.617634, 'Fusion Café', 'Modern café offering a blend of international dishes with a creative twist.', TRUE, 4.5, 1600, 8000, '10:00:00', '22:00:00', 'Vykhino St, 9', ARRAY[1, 9], TRUE),
    (uuid_generate_v4(), 55.758770, 37.628476, 'Bistro Bella', 'Stylish bistro offering a mix of European and Russian dishes.', TRUE, 4.4, 1400, 7000, '12:00:00', '23:00:00', 'Krasnopresnenskaya St, 8', ARRAY[2, 6], TRUE),
    (uuid_generate_v4(), 55.746342, 37.621490, 'Café Del Mar', 'Beach-themed café offering seafood and tropical drinks.', TRUE, 4.5, 2000, 10000, '11:00:00', '22:00:00', 'Leninsky Ave, 14', ARRAY[3, 8], TRUE),
    (uuid_generate_v4(), 55.732913, 37.588227, 'Urban Eats', 'Modern eatery with a focus on fresh, seasonal ingredients and international cuisine.', TRUE, 4.3, 1600, 9000, '11:00:00', '22:00:00', 'Petrovsko-Razumovskaya Alley, 15', ARRAY[1, 7], TRUE),
    (uuid_generate_v4(), 55.722818, 37.614600, 'Fresco', 'Upscale dining with a focus on Mediterranean cuisine and fine wines.', TRUE, 4.6, 2000, 12000, '12:00:00', '23:00:00', 'Kolomenskaya St, 6', ARRAY[2, 9], TRUE),
    (uuid_generate_v4(), 55.740821, 37.594764, 'Pizza Corner', 'Popular pizza joint offering a variety of pizzas and Italian dishes.', TRUE, 4.2, 1000, 5000, '10:00:00', '22:00:00', 'Donskoy St, 20', ARRAY[3, 6], TRUE),
    (uuid_generate_v4(), 55.758919, 37.619072, 'Sweet Life', 'Charming bakery and café specializing in desserts and pastries.', TRUE, 4.5, 1200, 6000, '09:00:00', '19:00:00', 'Svetlanskaya St, 10', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.746892, 37.623548, 'Green Garden', 'Restaurant focused on fresh, organic ingredients and healthy meals.', TRUE, 4.4, 1400, 7000, '11:00:00', '22:00:00', 'Moscow Center, 12', ARRAY[2, 7], TRUE),
    (uuid_generate_v4(), 55.732097, 37.592314, 'The Cozy Corner', 'Casual café offering a range of comfort foods and relaxed ambiance.', TRUE, 4.3, 1200, 5000, '10:00:00', '22:00:00', 'Victory Park, 5', ARRAY[3, 6], TRUE),
    (uuid_generate_v4(), 55.758173, 37.620958, 'Classic Grill', 'Casual dining spot known for its grilled meats and hearty sides.', TRUE, 4.4, 1600, 8000, '12:00:00', '22:00:00', 'Kuznetsky Most, 8', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.732810, 37.612450, 'Vikings', 'Restaurant offering Scandinavian cuisine with a modern twist.', TRUE, 4.5, 2000, 10000, '12:00:00', '23:00:00', 'Dmitrovskaya St, 11', ARRAY[2, 9], TRUE),
    (uuid_generate_v4(), 55.747620, 37.621940, 'Modern Diner', 'Trendy diner serving a mix of American and European comfort food.', TRUE, 4.3, 1400, 7000, '10:00:00', '23:00:00', 'Vokzalnaya St, 8', ARRAY[3, 6], TRUE),
    (uuid_generate_v4(), 55.724312, 37.612009, 'Harmony Bistro', 'Bistro offering a variety of dishes with a focus on healthy and balanced meals.', TRUE, 4.4, 1600, 8000, '11:00:00', '22:00:00', 'Luzhniki St, 11', ARRAY[1, 7], TRUE),
    (uuid_generate_v4(), 55.755830, 37.624781, 'Gourmet Delight', 'Upscale dining venue with an emphasis on gourmet international cuisine.', TRUE, 4.6, 2500, 15000, '12:00:00', '23:00:00', 'Tverskaya St, 14', ARRAY[2, 9], TRUE),
    (uuid_generate_v4(), 55.748134, 37.623546, 'East Wind', 'Asian restaurant specializing in fresh and flavorful dishes.', TRUE, 4.5, 1800, 8000, '12:00:00', '23:00:00', 'Yaroslavl Hwy, 22', ARRAY[3, 6], TRUE),
    (uuid_generate_v4(), 55.722760, 37.606174, 'Bella Vita', 'Charming restaurant offering a range of Italian classics and fine wines.', TRUE, 4.4, 1500, 7000, '11:00:00', '22:00:00', 'Marina Grove, 14', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.758960, 37.613447, 'The Italian Spot', 'Cozy Italian restaurant known for its authentic dishes and warm ambiance.', TRUE, 4.3, 1200, 6000, '10:00:00', '22:00:00', 'Tverskaya St, 16', ARRAY[2, 7], TRUE),
    (uuid_generate_v4(), 55.735978, 37.592848, 'Bistro Café', 'Relaxed café offering a variety of sandwiches, salads, and baked goods.', TRUE, 4.2, 800, 4000, '09:00:00', '20:00:00', 'Pechatniki St, 8', ARRAY[3, 6], TRUE),
    (uuid_generate_v4(), 55.744647, 37.623749, 'Sky Bar', 'Trendy bar with a rooftop terrace and panoramic city views.', TRUE, 4.6, 2000, 12000, '16:00:00', '02:00:00', 'Novoslobodskaya St, 7', ARRAY[1, 10], TRUE),
    (uuid_generate_v4(), 55.769063, 37.613299, 'Gastronomica', 'Gourmet restaurant focusing on innovative cuisine and elegant dining.', TRUE, 4.6, 2000, 13000, '11:00:00', '22:00:00', 'Svetlanskaya St, 5', ARRAY[2, 10], TRUE),
    (uuid_generate_v4(), 55.751483, 37.617332, 'Art-Café', 'Cafe offering a creative menu and artistic atmosphere.', TRUE, 4.3, 1000, 5000, '10:00:00', '22:00:00', 'Strastnoy Blvd, 11', ARRAY[1, 6], TRUE),
    (uuid_generate_v4(), 55.751784, 37.616590, 'Grill House', 'Casual spot known for its barbecue and steak options.', TRUE, 4.4, 1600, 9000, '12:00:00', '23:00:00', 'Arbat St, 12', ARRAY[3, 8], TRUE),
    (uuid_generate_v4(), 55.759887, 37.621267, 'Italiano', 'Restaurant serving classic Italian dishes with a contemporary twist.', TRUE, 4.5, 1800, 8000, '12:00:00', '22:00:00', 'Tverskaya St, 4', ARRAY[1, 9], TRUE),
    (uuid_generate_v4(), 55.752606, 37.621500, 'Café Noire', 'Upscale café known for its coffee and refined desserts.', TRUE, 4.6, 1500, 7000, '09:00:00', '20:00:00', 'Svetlanskaya St, 8', ARRAY[2, 10], TRUE),
    (uuid_generate_v4(), 55.759658, 37.629146, 'Royal Bar', 'Bar offering a selection of fine spirits and sophisticated cocktails.', TRUE, 4.4, 2000, 10000, '16:00:00', '02:00:00', 'Tverskaya St, 20', ARRAY[3, 6], TRUE),
    (uuid_generate_v4(), 55.752197, 37.610542, 'Kafe Garmonia', 'Cafe with a focus on healthy eating and relaxed atmosphere.', TRUE, 4.3, 800, 3500, '08:00:00', '19:00:00', 'Chistoprudny Blvd, 9', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.748974, 37.621688, 'The Kitchen', 'Casual eatery with a diverse menu and laid-back vibe.', TRUE, 4.2, 1200, 6000, '10:00:00', '22:00:00', 'Arbat St, 6', ARRAY[2, 7], TRUE),
    (uuid_generate_v4(), 55.747236, 37.593670, 'La Maison', 'French restaurant offering a variety of gourmet dishes in a chic setting.', TRUE, 4.5, 2500, 12000, '12:00:00', '23:00:00', 'Kutuzovsky Ave, 14', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.766081, 37.614805, 'Moscow Momo', 'Restaurant specializing in traditional Tibetan dumplings with a modern twist.', TRUE, 4.3, 1000, 5000, '10:00:00', '22:00:00', 'Bagrationovsky Ave, 16', ARRAY[3, 9], TRUE),
    (uuid_generate_v4(), 55.732751, 37.564732, 'Orangery', 'Elegant venue with a diverse menu focusing on international cuisine.', TRUE, 4.6, 2000, 12000, '12:00:00', '23:00:00', 'Prospekt Vernadskogo, 3', ARRAY[1, 7], TRUE),
    (uuid_generate_v4(), 55.715431, 37.585368, 'The Garden', 'Charming café known for its fresh juices and light snacks.', TRUE, 4.2, 800, 4000, '08:00:00', '20:00:00', 'Luzhniki St, 9', ARRAY[2, 6], TRUE),
    (uuid_generate_v4(), 55.788831, 37.650509, 'Sky Lounge', 'High-altitude bar offering stunning views of Moscow with a relaxed atmosphere.', TRUE, 4.7, 3000, 15000, '18:00:00', '02:00:00', 'Vostochnoe Chertanovo, 17', ARRAY[3, 10], TRUE),
    (uuid_generate_v4(), 55.715556, 37.608248, 'Zen Café', 'Trendy café with a focus on Japanese cuisine and specialty coffee.', TRUE, 4.4, 1200, 6000, '10:00:00', '21:00:00', 'Tsaritsyno St, 11', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.724793, 37.598907, 'Peking Duck', 'Authentic Chinese restaurant known for its Peking Duck and dim sum.', TRUE, 4.3, 1500, 8000, '11:00:00', '23:00:00', 'Tsaritsyno St, 14', ARRAY[2, 6], TRUE),
    (uuid_generate_v4(), 55.791376, 37.629204, 'Bistro 54', 'Casual bistro with a variety of dishes from European to Asian cuisine.', TRUE, 4.5, 1800, 7000, '11:00:00', '22:00:00', 'Izmailovo St, 7', ARRAY[3, 9], TRUE),
    (uuid_generate_v4(), 55.705812, 37.594840, 'Craft Beer House', 'Specialty bar offering a range of craft beers and pub food.', TRUE, 4.4, 1400, 6000, '12:00:00', '23:00:00', 'Sirius St, 4', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.751213, 37.623587, 'Café Bella', 'Italian café offering pizzas and pastas in a cozy setting.', TRUE, 4.2, 1200, 5000, '10:00:00', '22:00:00', 'Gorky St, 15', ARRAY[2, 7], TRUE),
    (uuid_generate_v4(), 55.744933, 37.600560, 'Veranda', 'Elegant restaurant with an emphasis on seafood and fine dining.', TRUE, 4.6, 2500, 12000, '12:00:00', '22:00:00', 'Yuzhnaya Ave, 23', ARRAY[1, 9], TRUE),
    (uuid_generate_v4(), 55.738042, 37.615089, 'Moscow Grill', 'Grill house offering a range of barbecue dishes and hearty meals.', TRUE, 4.3, 1600, 7000, '11:00:00', '22:00:00', 'Kotelniki St, 8', ARRAY[2, 6], TRUE),
    (uuid_generate_v4(), 55.758964, 37.619747, 'Tea Room', 'Classic tea house with a wide selection of teas and pastries.', TRUE, 4.2, 1000, 4000, '09:00:00', '20:00:00', 'Kropotkinsky St, 6', ARRAY[3, 8], TRUE),
    (uuid_generate_v4(), 55.754113, 37.624279, 'Gourmet Burger', 'Burgers and sides with gourmet flair and a relaxed atmosphere.', TRUE, 4.4, 1300, 6000, '11:00:00', '23:00:00', 'Patriarch Ponds, 7', ARRAY[1, 9], TRUE),
    (uuid_generate_v4(), 55.740675, 37.589220, 'Russian Feast', 'Restaurant offering a full range of traditional Russian dishes.', TRUE, 4.5, 1800, 9000, '12:00:00', '22:00:00', 'Moscow Center, 20', ARRAY[2, 7], TRUE),
    (uuid_generate_v4(), 55.764422, 37.622840, 'The Rooftop', 'Restaurant with a rooftop terrace offering panoramic views and fine dining.', TRUE, 4.7, 3000, 15000, '12:00:00', '23:00:00', 'Krasnopresnenskaya St, 10', ARRAY[1, 10], TRUE),
    (uuid_generate_v4(), 55.743095, 37.615746, 'Bistro Deluxe', 'Stylish bistro with an international menu and a focus on quality.', TRUE, 4.5, 2000, 10000, '10:00:00', '22:00:00', 'Tverskaya St, 18', ARRAY[2, 6], TRUE),
    (uuid_generate_v4(), 55.752657, 37.626891, 'Steakhouse 42', 'High-end steakhouse known for its premium cuts and refined atmosphere.', TRUE, 4.6, 2500, 15000, '12:00:00', '23:00:00', 'Gorky St, 8', ARRAY[3, 7], TRUE),
    (uuid_generate_v4(), 55.745092, 37.614442, 'Oriental Café', 'Café with a focus on Middle Eastern cuisine and vibrant decor.', TRUE, 4.4, 1400, 6000, '11:00:00', '22:00:00', 'Luzhniki St, 14', ARRAY[1, 8], TRUE),
    (uuid_generate_v4(), 55.751042, 37.615273, 'Almond Garden', 'Cafe offering a selection of pastries and light meals with a focus on quality ingredients.', TRUE, 4.3, 1200, 5000, '10:00:00', '21:00:00', 'Novoslobodskaya St, 5', ARRAY[2, 6], TRUE),
    (uuid_generate_v4(), 55.743915, 37.627150, 'Riverside Grill', 'Grill house located by the river offering a relaxed dining experience and scenic views.', TRUE, 4.5, 2000, 10000, '12:00:00', '23:00:00', 'Moscow Center, 9', ARRAY[3, 7], TRUE);
