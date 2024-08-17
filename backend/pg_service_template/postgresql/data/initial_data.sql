DROP SCHEMA IF EXISTS guide CASCADE;

CREATE SCHEMA IF NOT EXISTS guide;

CREATE TABLE IF NOT EXISTS guide.places (
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    approved BOOLEAN NOT NULL,
    rating REAL NOT NULL,
    price_lower_bound INTEGER NOT NULL,
    price_upper_bound INTEGER NOT NULL,
    open_time TIME NOT NULL,
    close_time TIME NOT NULL,
    address TEXT NOT NULL,
    tags varchar(50)[],
    score INTEGER DEFAULT 0,
    CHECK (rating >= 1.0 AND rating <= 5.0),
    CHECK (price_lower_bound > 0 AND price_lower_bound <= price_upper_bound)
);


CREATE TABLE IF NOT EXISTS guide.selections (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    is_collection BOOLEAN NOT NULL
);


CREATE TABLE IF NOT EXISTS guide.users (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   name TEXT NOT NULL,
   password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS guide.places_selections (
   place_id UUID,
   selection_id UUID,
   FOREIGN KEY(place_id) REFERENCES guide.places(id),
   FOREIGN KEY(selection_id) REFERENCES guide.selections(id)
);

CREATE TABLE IF NOT EXISTS guide.visibility (
    selection_id UUID NOT NULL,
    user_id UUID NOT NULL,
    FOREIGN KEY(user_id) REFERENCES guide.users(id),
    FOREIGN KEY(selection_id) REFERENCES guide.selections(id)
);

CREATE TABLE IF NOT EXISTS guide.auth (
    user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL DEFAULT uuid_generate_v4(),
    started_at TIMESTAMPTZ NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL
);

INSERT INTO guide.auth(user_id, session_id, started_at, expires_at) VALUES
    ('61846daf-3303-465e-ab7f-4af27d3e8f41', '5142cece-b22e-4a4f-adf9-990949d053ff','2024-08-10 14:30:45+03', '2024-09-10 22:30:45+03');

INSERT INTO guide.users(id, name, password) VALUES
('61846daf-3303-465e-ab7f-4af27d3e8f41', 'John', '1234');

INSERT INTO guide.places(
            id,
            lat,
            lon,
            name,
            description,
            approved,
            rating,
            price_lower_bound,
            price_upper_bound,
            open_time,
            close_time,
            address,
            tags,
            score
)
VALUES
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 55.736310, 37.596820, 'Brasserie Lambic', 'Brasserie Lambic — это бельгийский ресторан, расположенный недалеко от станции метро Парк культуры Сокольнической линии. В ресторане есть два зала, один из которых предназначен для курения кальяна, а другой — для тех, кто предпочитает курить электронные сигареты.', TRUE, 4.9, 1500, 2000, '12:00', '00:00', 'Турчанинов пер., 3, стр. 5', ARRAY['Семейный ужин с детьми'], 0),
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 55.723900, 37.588220, 'Sapiens', 'Sapiens — это ресторан с уникальной концепцией эволюции питания человека от древнейших времен до современности.', TRUE, 4.9, 2500, 5000, '09:00', '00:00', 'ул. Льва Толстого, 16', ARRAY['Бизнес-ланч', 'В тренде', 'Выпить кофе'], 1),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 55.733097, 37.592294, 'Папа Джонс', '«Папа Джонс» — это пиццерия, которая предлагает своим гостям широкий выбор пицц на любой вкус', FALSE, 4.2, 500, 500, '10:00', '3:00', 'Комсомольский просп., 4', '{}', 2),
    (uuid_generate_v4(), 55.751244, 37.618423, 'Pushkin Cafe', 'One of Moscow’s most famous cafes, known for its exquisite pastries and coffee.', TRUE, 4.5, 1500, 5000, '08:00:00', '22:00:00', 'Tverskoy Blvd, 26А', ARRAY['Бизнес-ланч', 'В тренде'], 3),
    (uuid_generate_v4(), 55.754999, 37.620437, 'White Rabbit', 'A high-end restaurant with stunning views and contemporary Russian cuisine.', TRUE, 4.7, 5000, 20000, '12:00:00', '23:00:00', 'Smolenskaya Square, 3', ARRAY['В тренде', 'Ресторан', 'Открытая кухня'], 4),
    (uuid_generate_v4(), 55.759303, 37.617536, 'Mari Vanna', 'A cozy restaurant offering traditional Russian dishes in a home-like setting.', TRUE, 4.4, 2000, 8000, '11:00:00', '23:00:00', 'Tverskaya St, 18', ARRAY['Выпить кофе', 'Бар'], 5),
    (uuid_generate_v4(), 55.749229, 37.606104, 'Dr. Zhivago', 'Classic Russian cuisine in a stylish, retro setting.', TRUE, 4.3, 1800, 9000, '10:00:00', '22:00:00', 'Strastnoy Blvd, 8', ARRAY['Завтрак', 'Выпить кофе', 'Семейный ужин с детьми'], 6),
    (uuid_generate_v4(), 55.751774, 37.616381, 'Beluga', 'Upscale restaurant known for its caviar and seafood dishes.', TRUE, 4.6, 2500, 15000, '12:00:00', '23:00:00', 'Neglinnaya St, 4', ARRAY['ULTIMA GUIDE', 'Семейный ужин с детьми'], 7),
    (uuid_generate_v4(), 55.757685, 37.621327, 'The Ribs', 'Casual spot famous for its ribs and American BBQ.', TRUE, 4.2, 1200, 6000, '11:00:00', '23:00:00', 'Tverskaya St, 25', ARRAY['Открытая кухня', 'Выпить кофе'], 8),
    (uuid_generate_v4(), 55.754569, 37.621639, 'Café Pushkin', 'Renowned café offering a range of classic Russian and European dishes.', TRUE, 4.5, 1500, 8000, '09:00:00', '22:00:00', 'Tverskoy Blvd, 26', ARRAY['Бар', 'Выпить кофе', 'Ресторан'], 9),
    (uuid_generate_v4(), 55.759191, 37.616152, 'Varenichnaya №1', 'Popular for its dumplings and traditional Russian comfort food.', TRUE, 4.3, 1000, 5000, '10:00:00', '23:00:00', 'Arbat St, 1', ARRAY['Выпить кофе', 'Открытая кухня', 'В тренде'], 10),
    (uuid_generate_v4(), 55.758167, 37.621137, 'LavkaLavka', 'Farm-to-table restaurant focused on organic and locally sourced ingredients.', TRUE, 4.4, 1500, 7000, '10:00:00', '22:00:00', 'Svetlanskaya St, 14', ARRAY['Семейный ужин с детьми', 'Бар', 'Завтрак'], 11),
    (uuid_generate_v4(), 55.750794, 37.618121, 'Moscow Pizza', 'A casual spot for pizza and Italian cuisine.', TRUE, 4.1, 1000, 5000, '11:00:00', '23:00:00', 'Tverskoy Blvd, 24', ARRAY['Бизнес-ланч'], 12),
    (uuid_generate_v4(), 55.751263, 37.616226, 'Gorynych', 'Restaurant offering traditional Russian fare with a cozy ambiance.', TRUE, 4.4, 1400, 7000, '12:00:00', '22:00:00', 'Arbat St, 21', ARRAY['Выпить кофе', 'Бизнес-ланч'], 13),
    (uuid_generate_v4(), 55.757984, 37.621834, 'Café Sova', 'Modern café known for its coffee and light bites.', TRUE, 4.2, 800, 3000, '08:00:00', '20:00:00', 'Chistoprudny Blvd, 2', ARRAY['Семейный ужин с детьми', 'Бар', 'Ресторан'], 14),
    (uuid_generate_v4(), 55.759410, 37.619098, 'Stroganoff Steakhouse', 'High-end steakhouse offering premium cuts and fine dining experience.', TRUE, 4.5, 2000, 12000, '12:00:00', '23:00:00', 'Tverskaya St, 10', ARRAY['Завтрак', 'Бар'], 15),
    (uuid_generate_v4(), 55.755415, 37.617592, 'Lavka', 'An organic restaurant with a focus on healthy, farm-fresh dishes.', TRUE, 4.3, 1600, 9000, '11:00:00', '22:00:00', 'Kudrinskaya Sq, 8', ARRAY['ULTIMA GUIDE', 'Выпить кофе', 'Завтрак', 'Бизнес-ланч'], 16),
    (uuid_generate_v4(), 55.752755, 37.617531, 'Cherdak', 'Rustic café with a focus on homemade food and traditional recipes.', TRUE, 4.4, 1200, 6000, '09:00:00', '21:00:00', 'Chistoprudny Blvd, 12', ARRAY['Выпить кофе'], 17),
    (uuid_generate_v4(), 55.754838, 37.620007, 'O2 Lounge', 'A stylish rooftop bar offering panoramic views of Moscow.', TRUE, 4.6, 3000, 20000, '18:00:00', '02:00:00', 'Okhotny Ryad, 2', ARRAY['Завтрак', 'Бар', 'В тренде', 'ULTIMA GUIDE'], 18),
    (uuid_generate_v4(), 55.757251, 37.616358, 'Craft Kitchen', 'Modern eatery specializing in craft beer and gourmet burgers.', TRUE, 4.5, 1800, 10000, '12:00:00', '23:00:00', 'Strastnoy Blvd, 6', ARRAY['Бизнес-ланч', 'Выпить кофе', 'Завтрак'], 19),
    (uuid_generate_v4(), 55.751826, 37.617832, 'The Hoxton', 'A trendy restaurant offering contemporary dishes and stylish decor.', TRUE, 4.3, 1500, 8000, '11:00:00', '23:00:00', 'Tverskaya St, 22', ARRAY['ULTIMA GUIDE', 'Выпить кофе', 'Бар', 'Ресторан'], 20),
    (uuid_generate_v4(), 55.750119, 37.618568, 'Gostinny Dvor', 'A historic restaurant serving traditional Russian dishes.', TRUE, 4.6, 2000, 12000, '12:00:00', '22:00:00', 'Tverskoy Blvd, 18', ARRAY['Бизнес-ланч', 'Выпить кофе', 'В тренде'], 21),
    (uuid_generate_v4(), 55.755972, 37.620188, 'Burger King', 'International fast-food chain known for its burgers and fries.', TRUE, 4.2, 600, 2500, '10:00:00', '22:00:00', 'Tverskaya St, 5', ARRAY['Бар'], 22),
    (uuid_generate_v4(), 55.757238, 37.618741, 'Chayhona №1', 'Popular chain offering traditional Central Asian cuisine.', TRUE, 4.3, 1200, 6000, '10:00:00', '22:00:00', 'Arbat St, 7', ARRAY['Выпить кофе'], 23),
    (uuid_generate_v4(), 55.754512, 37.617654, 'Bar & Grill', 'Relaxed venue for grilled meats and craft beers.', TRUE, 4.4, 1400, 7000, '12:00:00', '23:00:00', 'Kudrinskaya Sq, 6', ARRAY['Бизнес-ланч', 'Бар'], 24),
    (uuid_generate_v4(), 55.755433, 37.619882, 'TGI Fridays', 'American restaurant chain with a casual atmosphere and extensive menu.', TRUE, 4.2, 1500, 8000, '11:00:00', '23:00:00', 'Pushkinskaya St, 3', ARRAY['ULTIMA GUIDE', 'Семейный ужин с детьми', 'Завтрак', 'Бар'], 25),
    (uuid_generate_v4(), 55.758790, 37.620919, 'Gastronomica', 'Gourmet restaurant focusing on innovative cuisine and elegant dining.', TRUE, 4.6, 2000, 13000, '11:00:00', '22:00:00', 'Svetlanskaya St, 5', ARRAY['ULTIMA GUIDE'], 26),
    (uuid_generate_v4(), 55.752512, 37.619546, 'Art-Café', 'Cafe offering a creative menu and artistic atmosphere.', TRUE, 4.3, 1000, 5000, '10:00:00', '22:00:00', 'Strastnoy Blvd, 11', ARRAY['Выпить кофе', 'Ресторан', 'Завтрак', 'Бизнес-ланч'], 27),
    (uuid_generate_v4(), 55.753388, 37.616854, 'Grill House', 'Casual spot known for its barbecue and steak options.', TRUE, 4.4, 1600, 9000, '12:00:00', '23:00:00', 'Arbat St, 12', ARRAY['В тренде', 'Завтрак', 'Выпить кофе'], 28),
    (uuid_generate_v4(), 55.755610, 37.621540, 'Italiano', 'Restaurant serving classic Italian dishes with a contemporary twist.', TRUE, 4.5, 1800, 8000, '12:00:00', '22:00:00', 'Tverskaya St, 4', ARRAY['Завтрак', 'Бизнес-ланч', 'Бар'], 29),
    (uuid_generate_v4(), 55.759556, 37.619510, 'Café Noire', 'Upscale café known for its coffee and refined desserts.', TRUE, 4.6, 1500, 7000, '09:00:00', '20:00:00', 'Svetlanskaya St, 8', ARRAY['Завтрак', 'Бизнес-ланч', 'Семейный ужин с детьми', 'Бар'], 30),
    (uuid_generate_v4(), 55.754773, 37.620932, 'Royal Bar', 'Bar offering a selection of fine spirits and sophisticated cocktails.', TRUE, 4.4, 2000, 10000, '16:00:00', '02:00:00', 'Tverskaya St, 20', ARRAY['Бар', 'Выпить кофе', 'ULTIMA GUIDE'], 31),
    (uuid_generate_v4(), 55.757901, 37.621051, 'Kafe Garmonia', 'Cafe with a focus on healthy eating and relaxed atmosphere.', TRUE, 4.3, 800, 3500, '08:00:00', '19:00:00', 'Chistoprudny Blvd, 9', ARRAY['Выпить кофе', 'Бар'], 32),
    (uuid_generate_v4(), 55.755844, 37.619275, 'The Kitchen', 'Casual eatery with a diverse menu and laid-back vibe.', TRUE, 4.2, 1200, 6000, '10:00:00', '22:00:00', 'Arbat St, 6', ARRAY['Открытая кухня', 'Ресторан', 'Семейный ужин с детьми'], 33),
    (uuid_generate_v4(), 55.746594, 37.620920, 'La Maison', 'French restaurant offering a variety of gourmet dishes in a chic setting.', TRUE, 4.5, 2500, 12000, '12:00:00', '23:00:00', 'Kutuzovsky Ave, 14', ARRAY['ULTIMA GUIDE', 'Выпить кофе'], 34),
    (uuid_generate_v4(), 55.765335, 37.629845, 'Moscow Momo', 'Restaurant specializing in traditional Tibetan dumplings with a modern twist.', TRUE, 4.3, 1000, 5000, '10:00:00', '22:00:00', 'Bagrationovsky Ave, 16', ARRAY['Выпить кофе', 'Ресторан', 'Бизнес-ланч'], 35),
    (uuid_generate_v4(), 55.740350, 37.580209, 'Orangery', 'Elegant venue with a diverse menu focusing on international cuisine.', TRUE, 4.6, 2000, 12000, '12:00:00', '23:00:00', 'Prospekt Vernadskogo, 3', ARRAY['Открытая кухня', 'Завтрак'], 36),
    (uuid_generate_v4(), 55.727409, 37.583264, 'The Garden', 'Charming café known for its fresh juices and light snacks.', TRUE, 4.2, 800, 4000, '08:00:00', '20:00:00', 'Luzhniki St, 9', ARRAY['Бизнес-ланч', 'Семейный ужин с детьми', 'Открытая кухня'], 37),
    (uuid_generate_v4(), 55.790000, 37.640000, 'Sky Lounge', 'High-altitude bar offering stunning views of Moscow with a relaxed atmosphere.', TRUE, 4.7, 3000, 15000, '18:00:00', '02:00:00', 'Vostochnoe Chertanovo, 17', ARRAY['Бизнес-ланч', 'Завтрак', 'Ресторан'], 38),
    (uuid_generate_v4(), 55.711256, 37.612658, 'Zen Café', 'Trendy café with a focus on Japanese cuisine and specialty coffee.', TRUE, 4.4, 1200, 6000, '10:00:00', '21:00:00', 'Tsaritsyno St, 11', ARRAY['Завтрак'], 39),
    (uuid_generate_v4(), 55.723145, 37.607783, 'Peking Duck', 'Authentic Chinese restaurant known for its Peking Duck and dim sum.', TRUE, 4.3, 1500, 8000, '11:00:00', '23:00:00', 'Tsaritsyno St, 14', ARRAY['ULTIMA GUIDE', 'В тренде'], 40),
    (uuid_generate_v4(), 55.788922, 37.636817, 'Bistro 54', 'Casual bistro with a variety of dishes from European to Asian cuisine.', TRUE, 4.5, 1800, 7000, '11:00:00', '22:00:00', 'Izmailovo St, 7', ARRAY['Бизнес-ланч', 'Бар', 'ULTIMA GUIDE'], 41),
    (uuid_generate_v4(), 55.702391, 37.601164, 'Craft Beer House', 'Specialty bar offering a range of craft beers and pub food.', TRUE, 4.4, 1400, 6000, '12:00:00', '23:00:00', 'Sirius St, 4', ARRAY['Открытая кухня', 'Бизнес-ланч', 'Бар'], 42),
    (uuid_generate_v4(), 55.752817, 37.614787, 'Café Bella', 'Italian café offering pizzas and pastas in a cozy setting.', TRUE, 4.2, 1200, 5000, '10:00:00', '22:00:00', 'Gorky St, 15', ARRAY['Выпить кофе', 'Открытая кухня', 'В тренде', 'ULTIMA GUIDE', 'Ресторан'], 43),
    (uuid_generate_v4(), 55.742833, 37.584598, 'Veranda', 'Elegant restaurant with an emphasis on seafood and fine dining.', TRUE, 4.6, 2500, 12000, '12:00:00', '22:00:00', 'Yuzhnaya Ave, 23', ARRAY['Завтрак', 'Бар'], 44),
    (uuid_generate_v4(), 55.738390, 37.622749, 'Moscow Grill', 'Grill house offering a range of barbecue dishes and hearty meals.', TRUE, 4.3, 1600, 7000, '11:00:00', '22:00:00', 'Kotelniki St, 8', ARRAY['Открытая кухня', 'ULTIMA GUIDE'], 45),
    (uuid_generate_v4(), 55.755665, 37.621314, 'Tea Room', 'Classic tea house with a wide selection of teas and pastries.', TRUE, 4.2, 1000, 4000, '09:00:00', '20:00:00', 'Kropotkinsky St, 6', ARRAY['Бар'], 46),
    (uuid_generate_v4(), 55.752622, 37.624509, 'Gourmet Burger', 'Burgers and sides with gourmet flair and a relaxed atmosphere.', TRUE, 4.4, 1300, 6000, '11:00:00', '23:00:00', 'Patriarch Ponds, 7', ARRAY['Семейный ужин с детьми', 'Завтрак'], 47),
    (uuid_generate_v4(), 55.738952, 37.594242, 'Russian Feast', 'Restaurant offering a full range of traditional Russian dishes.', TRUE, 4.5, 1800, 9000, '12:00:00', '22:00:00', 'Moscow Center, 20', ARRAY['Бар', 'Ресторан'], 48),
    (uuid_generate_v4(), 55.721458, 37.614823, 'Sushi House', 'Popular sushi restaurant with a modern menu and stylish decor.', TRUE, 4.6, 1500, 8000, '11:00:00', '23:00:00', 'Kolomenskoe St, 12', ARRAY['Ресторан'], 49),
    (uuid_generate_v4(), 55.741203, 37.620128, 'Dinner Club', 'Chic venue offering a diverse menu with an emphasis on fine dining.', TRUE, 4.3, 2000, 10000, '12:00:00', '23:00:00', 'Vernadsky Ave, 8', ARRAY['Бизнес-ланч', 'В тренде', 'Открытая кухня'], 50),
    (uuid_generate_v4(), 55.747829, 37.609751, 'Street Food', 'Casual eatery with a range of street food options from around the world.', TRUE, 4.4, 1000, 4000, '11:00:00', '22:00:00', 'Sokolniki St, 5', ARRAY['Семейный ужин с детьми', 'Открытая кухня', 'ULTIMA GUIDE', 'В тренде'], 51),
    (uuid_generate_v4(), 55.726171, 37.600349, 'Vino', 'Wine bar offering a selection of fine wines and gourmet small plates.', TRUE, 4.5, 1800, 7000, '12:00:00', '22:00:00', 'Marina Grove, 8', ARRAY['В тренде'], 52),
    (uuid_generate_v4(), 55.741275, 37.609184, 'Panorama', 'Restaurant with panoramic views and a menu focused on modern European cuisine.', TRUE, 4.6, 2000, 15000, '12:00:00', '23:00:00', 'Prospekt Mira, 18', ARRAY['Бизнес-ланч', 'Открытая кухня'], 53),
    (uuid_generate_v4(), 55.746517, 37.609943, 'The Tasty Corner', 'A casual dining spot with an emphasis on comfort food and friendly service.', TRUE, 4.3, 1200, 5000, '11:00:00', '22:00:00', 'Luzhniki St, 5', ARRAY['В тренде'], 54),
    (uuid_generate_v4(), 55.760123, 37.611498, 'Rustic Table', 'Restaurant known for its hearty meals and rustic decor.', TRUE, 4.4, 1400, 6000, '11:00:00', '22:00:00', 'Serebryany Bor, 2', ARRAY['Бар', 'ULTIMA GUIDE', 'Семейный ужин с детьми', 'Выпить кофе'], 55),
    (uuid_generate_v4(), 55.727059, 37.617634, 'Fusion Café', 'Modern café offering a blend of international dishes with a creative twist.', TRUE, 4.5, 1600, 8000, '10:00:00', '22:00:00', 'Vykhino St, 9', ARRAY['ULTIMA GUIDE', 'Открытая кухня', 'Семейный ужин с детьми'], 56),
    (uuid_generate_v4(), 55.758770, 37.628476, 'Bistro Bella', 'Stylish bistro offering a mix of European and Russian dishes.', TRUE, 4.4, 1400, 7000, '12:00:00', '23:00:00', 'Krasnopresnenskaya St, 8', ARRAY['Ресторан'], 57),
    (uuid_generate_v4(), 55.746342, 37.621490, 'Café Del Mar', 'Beach-themed café offering seafood and tropical drinks.', TRUE, 4.5, 2000, 10000, '11:00:00', '22:00:00', 'Leninsky Ave, 14', ARRAY['Завтрак', 'Открытая кухня'], 58),
    (uuid_generate_v4(), 55.732913, 37.588227, 'Urban Eats', 'Modern eatery with a focus on fresh, seasonal ingredients and international cuisine.', TRUE, 4.3, 1600, 9000, '11:00:00', '22:00:00', 'Petrovsko-Razumovskaya Alley, 15', ARRAY['ULTIMA GUIDE', 'Открытая кухня', 'Выпить кофе', 'Ресторан'], 59),
    (uuid_generate_v4(), 55.722818, 37.614600, 'Fresco', 'Upscale dining with a focus on Mediterranean cuisine and fine wines.', TRUE, 4.6, 2000, 12000, '12:00:00', '23:00:00', 'Kolomenskaya St, 6', ARRAY['Семейный ужин с детьми', 'Бизнес-ланч', 'Выпить кофе'], 60),
    (uuid_generate_v4(), 55.740821, 37.594764, 'Pizza Corner', 'Popular pizza joint offering a variety of pizzas and Italian dishes.', TRUE, 4.2, 1000, 5000, '10:00:00', '22:00:00', 'Donskoy St, 20', ARRAY['Семейный ужин с детьми', 'Выпить кофе', 'Завтрак'], 61),
    (uuid_generate_v4(), 55.758919, 37.619072, 'Sweet Life', 'Charming bakery and café specializing in desserts and pastries.', TRUE, 4.5, 1200, 6000, '09:00:00', '19:00:00', 'Svetlanskaya St, 10', ARRAY['Бизнес-ланч', 'Семейный ужин с детьми', 'Ресторан'], 62),
    (uuid_generate_v4(), 55.746892, 37.623548, 'Green Garden', 'Restaurant focused on fresh, organic ingredients and healthy meals.', TRUE, 4.4, 1400, 7000, '11:00:00', '22:00:00', 'Moscow Center, 12', ARRAY['Ресторан', 'ULTIMA GUIDE'], 63),
    (uuid_generate_v4(), 55.732097, 37.592314, 'The Cozy Corner', 'Casual café offering a range of comfort foods and relaxed ambiance.', TRUE, 4.3, 1200, 5000, '10:00:00', '22:00:00', 'Victory Park, 5', ARRAY['Бизнес-ланч', 'Ресторан', 'Открытая кухня', 'Бар'], 64),
    (uuid_generate_v4(), 55.758173, 37.620958, 'Classic Grill', 'Casual dining spot known for its grilled meats and hearty sides.', TRUE, 4.4, 1600, 8000, '12:00:00', '22:00:00', 'Kuznetsky Most, 8', ARRAY['Выпить кофе', 'Открытая кухня', 'В тренде', 'Ресторан'], 65),
    (uuid_generate_v4(), 55.732810, 37.612450, 'Vikings', 'Restaurant offering Scandinavian cuisine with a modern twist.', TRUE, 4.5, 2000, 10000, '12:00:00', '23:00:00', 'Dmitrovskaya St, 11', ARRAY['ULTIMA GUIDE'], 66),
    (uuid_generate_v4(), 55.747620, 37.621940, 'Modern Diner', 'Trendy diner serving a mix of American and European comfort food.', TRUE, 4.3, 1400, 7000, '10:00:00', '23:00:00', 'Vokzalnaya St, 8', ARRAY['Выпить кофе', 'Бар', 'В тренде'], 67),
    (uuid_generate_v4(), 55.724312, 37.612009, 'Harmony Bistro', 'Bistro offering a variety of dishes with a focus on healthy and balanced meals.', TRUE, 4.4, 1600, 8000, '11:00:00', '22:00:00', 'Luzhniki St, 11', ARRAY['Завтрак', 'Ресторан', 'ULTIMA GUIDE', 'Бар'], 68),
    (uuid_generate_v4(), 55.755830, 37.624781, 'Gourmet Delight', 'Upscale dining venue with an emphasis on gourmet international cuisine.', TRUE, 4.6, 2500, 15000, '12:00:00', '23:00:00', 'Tverskaya St, 14', ARRAY['Бар', 'Завтрак', 'Семейный ужин с детьми'], 69),
    (uuid_generate_v4(), 55.748134, 37.623546, 'East Wind', 'Asian restaurant specializing in fresh and flavorful dishes.', TRUE, 4.5, 1800, 8000, '12:00:00', '23:00:00', 'Yaroslavl Hwy, 22', ARRAY['Открытая кухня'], 70),
    (uuid_generate_v4(), 55.722760, 37.606174, 'Bella Vita', 'Charming restaurant offering a range of Italian classics and fine wines.', TRUE, 4.4, 1500, 7000, '11:00:00', '22:00:00', 'Marina Grove, 14', ARRAY['Бар', 'Открытая кухня', 'Бизнес-ланч'], 71),
    (uuid_generate_v4(), 55.758960, 37.613447, 'The Italian Spot', 'Cozy Italian restaurant known for its authentic dishes and warm ambiance.', TRUE, 4.3, 1200, 6000, '10:00:00', '22:00:00', 'Tverskaya St, 16', ARRAY['Завтрак', 'Ресторан', 'Выпить кофе'], 72),
    (uuid_generate_v4(), 55.735978, 37.592848, 'Bistro Café', 'Relaxed café offering a variety of sandwiches, salads, and baked goods.', TRUE, 4.2, 800, 4000, '09:00:00', '20:00:00', 'Pechatniki St, 8', ARRAY['Ресторан', 'ULTIMA GUIDE'], 73),
    (uuid_generate_v4(), 55.744647, 37.623749, 'Sky Bar', 'Trendy bar with a rooftop terrace and panoramic city views.', TRUE, 4.6, 2000, 12000, '16:00:00', '02:00:00', 'Novoslobodskaya St, 7', ARRAY['Выпить кофе', 'В тренде'], 74),
    (uuid_generate_v4(), 55.769063, 37.613299, 'Gastronomica', 'Gourmet restaurant focusing on innovative cuisine and elegant dining.', TRUE, 4.6, 2000, 13000, '11:00:00', '22:00:00', 'Svetlanskaya St, 5', ARRAY['Семейный ужин с детьми', 'Завтрак', 'Открытая кухня', 'Ресторан'], 75),
    (uuid_generate_v4(), 55.751483, 37.617332, 'Art-Café', 'Cafe offering a creative menu and artistic atmosphere.', TRUE, 4.3, 1000, 5000, '10:00:00', '22:00:00', 'Strastnoy Blvd, 11', ARRAY['В тренде', 'Семейный ужин с детьми'], 76),
    (uuid_generate_v4(), 55.751784, 37.616590, 'Grill House', 'Casual spot known for its barbecue and steak options.', TRUE, 4.4, 1600, 9000, '12:00:00', '23:00:00', 'Arbat St, 12', ARRAY['Выпить кофе', 'Семейный ужин с детьми', 'Ресторан', 'В тренде', 'Завтрак'], 77),
    (uuid_generate_v4(), 55.759887, 37.621267, 'Italiano', 'Restaurant serving classic Italian dishes with a contemporary twist.', TRUE, 4.5, 1800, 8000, '12:00:00', '22:00:00', 'Tverskaya St, 4', ARRAY['Бизнес-ланч', 'Бар', 'Выпить кофе'], 78),
    (uuid_generate_v4(), 55.752606, 37.621500, 'Café Noire', 'Upscale café known for its coffee and refined desserts.', TRUE, 4.6, 1500, 7000, '09:00:00', '20:00:00', 'Svetlanskaya St, 8', ARRAY['Бизнес-ланч'], 79),
    (uuid_generate_v4(), 55.759658, 37.629146, 'Royal Bar', 'Bar offering a selection of fine spirits and sophisticated cocktails.', TRUE, 4.4, 2000, 10000, '16:00:00', '02:00:00', 'Tverskaya St, 20', ARRAY['Ресторан', 'Завтрак'], 80),
    (uuid_generate_v4(), 55.752197, 37.610542, 'Kafe Garmonia', 'Cafe with a focus on healthy eating and relaxed atmosphere.', TRUE, 4.3, 800, 3500, '08:00:00', '19:00:00', 'Chistoprudny Blvd, 9', ARRAY['Завтрак', 'Бизнес-ланч', 'В тренде', 'Семейный ужин с детьми'], 81),
    (uuid_generate_v4(), 55.748974, 37.621688, 'The Kitchen', 'Casual eatery with a diverse menu and laid-back vibe.', TRUE, 4.2, 1200, 6000, '10:00:00', '22:00:00', 'Arbat St, 6', ARRAY['Выпить кофе', 'Бизнес-ланч', 'Завтрак'], 82),
    (uuid_generate_v4(), 55.747236, 37.593670, 'La Maison', 'French restaurant offering a variety of gourmet dishes in a chic setting.', TRUE, 4.5, 2500, 12000, '12:00:00', '23:00:00', 'Kutuzovsky Ave, 14', ARRAY['Бизнес-ланч', 'Бар', 'В тренде'], 83),
    (uuid_generate_v4(), 55.766081, 37.614805, 'Moscow Momo', 'Restaurant specializing in traditional Tibetan dumplings with a modern twist.', TRUE, 4.3, 1000, 5000, '10:00:00', '22:00:00', 'Bagrationovsky Ave, 16', ARRAY['В тренде', 'Бар', 'ULTIMA GUIDE', 'Ресторан'], 84),
    (uuid_generate_v4(), 55.732751, 37.564732, 'Orangery', 'Elegant venue with a diverse menu focusing on international cuisine.', TRUE, 4.6, 2000, 12000, '12:00:00', '23:00:00', 'Prospekt Vernadskogo, 3', ARRAY['Завтрак', 'Ресторан'], 85),
    (uuid_generate_v4(), 55.715431, 37.585368, 'The Garden', 'Charming café known for its fresh juices and light snacks.', TRUE, 4.2, 800, 4000, '08:00:00', '20:00:00', 'Luzhniki St, 9', ARRAY['В тренде', 'Открытая кухня'], 86),
    (uuid_generate_v4(), 55.788831, 37.650509, 'Sky Lounge', 'High-altitude bar offering stunning views of Moscow with a relaxed atmosphere.', TRUE, 4.7, 3000, 15000, '18:00:00', '02:00:00', 'Vostochnoe Chertanovo, 17', ARRAY['Ресторан', 'Открытая кухня', 'Семейный ужин с детьми'], 87),
    (uuid_generate_v4(), 55.715556, 37.608248, 'Zen Café', 'Trendy café with a focus on Japanese cuisine and specialty coffee.', TRUE, 4.4, 1200, 6000, '10:00:00', '21:00:00', 'Tsaritsyno St, 11', ARRAY['Бар', 'Выпить кофе'], 88),
    (uuid_generate_v4(), 55.724793, 37.598907, 'Peking Duck', 'Authentic Chinese restaurant known for its Peking Duck and dim sum.', TRUE, 4.3, 1500, 8000, '11:00:00', '23:00:00', 'Tsaritsyno St, 14', ARRAY['Семейный ужин с детьми', 'Завтрак', 'Ресторан', 'В тренде'], 89),
    (uuid_generate_v4(), 55.791376, 37.629204, 'Bistro 54', 'Casual bistro with a variety of dishes from European to Asian cuisine.', TRUE, 4.5, 1800, 7000, '11:00:00', '22:00:00', 'Izmailovo St, 7', ARRAY['Открытая кухня', 'Выпить кофе', 'Ресторан', 'Бизнес-ланч'], 90),
    (uuid_generate_v4(), 55.705812, 37.594840, 'Craft Beer House', 'Specialty bar offering a range of craft beers and pub food.', TRUE, 4.4, 1400, 6000, '12:00:00', '23:00:00', 'Sirius St, 4', ARRAY['Открытая кухня'], 91),
    (uuid_generate_v4(), 55.751213, 37.623587, 'Café Bella', 'Italian café offering pizzas and pastas in a cozy setting.', TRUE, 4.2, 1200, 5000, '10:00:00', '22:00:00', 'Gorky St, 15', ARRAY['Бар', 'В тренде', 'Бизнес-ланч'], 92),
    (uuid_generate_v4(), 55.744933, 37.600560, 'Veranda', 'Elegant restaurant with an emphasis on seafood and fine dining.', TRUE, 4.6, 2500, 12000, '12:00:00', '22:00:00', 'Yuzhnaya Ave, 23', ARRAY['Ресторан', 'Открытая кухня', 'В тренде'], 93),
    (uuid_generate_v4(), 55.738042, 37.615089, 'Moscow Grill', 'Grill house offering a range of barbecue dishes and hearty meals.', TRUE, 4.3, 1600, 7000, '11:00:00', '22:00:00', 'Kotelniki St, 8', ARRAY['Бизнес-ланч', 'ULTIMA GUIDE'], 94),
    (uuid_generate_v4(), 55.758964, 37.619747, 'Tea Room', 'Classic tea house with a wide selection of teas and pastries.', TRUE, 4.2, 1000, 4000, '09:00:00', '20:00:00', 'Kropotkinsky St, 6', ARRAY['Семейный ужин с детьми'], 95),
    (uuid_generate_v4(), 55.754113, 37.624279, 'Gourmet Burger', 'Burgers and sides with gourmet flair and a relaxed atmosphere.', TRUE, 4.4, 1300, 6000, '11:00:00', '23:00:00', 'Patriarch Ponds, 7', ARRAY['Выпить кофе', 'Бизнес-ланч'], 96),
    (uuid_generate_v4(), 55.740675, 37.589220, 'Russian Feast', 'Restaurant offering a full range of traditional Russian dishes.', TRUE, 4.5, 1800, 9000, '12:00:00', '22:00:00', 'Moscow Center, 20', ARRAY['Бар', 'Выпить кофе', 'Ресторан'], 97),
    (uuid_generate_v4(), 55.764422, 37.622840, 'The Rooftop', 'Restaurant with a rooftop terrace offering panoramic views and fine dining.', TRUE, 4.7, 3000, 15000, '12:00:00', '23:00:00', 'Krasnopresnenskaya St, 10', ARRAY['Завтрак', 'Выпить кофе', 'ULTIMA GUIDE', 'Семейный ужин с детьми'], 98),
    (uuid_generate_v4(), 55.743095, 37.615746, 'Bistro Deluxe', 'Stylish bistro with an international menu and a focus on quality.', TRUE, 4.5, 2000, 10000, '10:00:00', '22:00:00', 'Tverskaya St, 18', ARRAY['ULTIMA GUIDE', 'Бизнес-ланч', 'Открытая кухня', 'Семейный ужин с детьми', 'Бар'], 99),
    (uuid_generate_v4(), 55.752657, 37.626891, 'Steakhouse 42', 'High-end steakhouse known for its premium cuts and refined atmosphere.', TRUE, 4.6, 2500, 15000, '12:00:00', '23:00:00', 'Gorky St, 8', ARRAY['Выпить кофе'], 100),
    (uuid_generate_v4(), 55.745092, 37.614442, 'Oriental Café', 'Café with a focus on Middle Eastern cuisine and vibrant decor.', TRUE, 4.4, 1400, 6000, '11:00:00', '22:00:00', 'Luzhniki St, 14', ARRAY['Выпить кофе', 'В тренде', 'Завтрак'], 101),
    (uuid_generate_v4(), 55.751042, 37.615273, 'Almond Garden', 'Cafe offering a selection of pastries and light meals with a focus on quality ingredients.', TRUE, 4.3, 1200, 5000, '10:00:00', '21:00:00', 'Novoslobodskaya St, 5', ARRAY['ULTIMA GUIDE', 'Бар', 'Открытая кухня'], 102),
    (uuid_generate_v4(), 55.743915, 37.627150, 'Riverside Grill', 'Grill house located by the river offering a relaxed dining experience and scenic views.', TRUE, 4.5, 2000, 10000, '12:00:00', '23:00:00', 'Moscow Center, 9', ARRAY['Ресторан', 'Открытая кухня'], 103),
    ('5794d6b8-931a-492c-a08b-9cf0aba52bcc', 55.738570, 37.581200, 'Кооператив Чёрный', 'Уютная кофейня с авторскими напитками и минималистичным интерьером.', TRUE, 4.9, 300, 800, '08:00', '20:00', 'ул. Тимура Фрунзе, 24', ARRAY['Ресторан', 'Открытая кухня'], 104),
    ('41a4a248-21c4-4eb0-8a01-6deeb7207b92', 55.734710, 37.590420, 'Coffee Bean', 'Известная сеть кофеен с большим выбором кофе и десертов.', TRUE, 4.7, 400, 900, '07:00', '21:00', 'ул. Остоженка, 31', ARRAY['В тренде'], 105),
    ('71a12089-32ec-4f3d-a872-c740291524ff', 55.739800, 37.599520, 'Breakfast Café', 'Заведение с европейской кухней, специализирующееся на завтраках.', TRUE, 4.8, 500, 1500, '08:00', '13:00', 'ул. Б. Никитская, 10', ARRAY['Бизнес-ланч', 'Выпить кофе', 'ULTIMA GUIDE', 'Открытая кухня'], 106),
    ('e0f1e615-ab7f-49a3-9594-27907a91ffd1', 55.732180, 37.592770, 'Кафе Pushkin', 'Легендарное кафе с утонченным интерьером и европейским меню.', TRUE, 4.9, 2000, 4000, '08:00', '14:00', 'Тверской б-р, 26', ARRAY['В тренде', 'Открытая кухня', 'Бар'], 107),
    ('245e864e-f264-4222-aca7-416899098386', 55.735500, 37.602180, 'Домашний Очаг', 'Уютное кафе с домашней кухней, особенно популярное на завтрак.', TRUE, 4.7, 400, 1200, '09:00', '12:00', 'ул. Остоженка, 38', ARRAY['Бизнес-ланч'], 108),
    ('6c7584cc-1933-4183-95a9-6b90f54fb10e', 55.737190, 37.583890, 'Теплый Дом', 'Кафе с домашними блюдами и теплыми напитками.', TRUE, 4.6, 300, 1100, '09:00', '11:00', 'ул. Арбат, 15', ARRAY['Ресторан', 'Выпить кофе', 'Семейный ужин с детьми'], 109),
    ('7e4adcef-25f4-4bcc-ba3c-a743fa8d78a7', 55.733300, 37.591620, 'Трапезная', 'Столовая с недорогими блюдами и обедами.', TRUE, 4.5, 200, 500, '10:00', '15:00', 'ул. Пятницкая, 30', ARRAY['Выпить кофе', 'Бар', 'Завтрак'], 110),
    ('97b3f9cc-93af-4c27-9429-03d476dc38db', 55.736470, 37.598370, 'Гастроном', 'Небольшая столовая с домашней атмосферой.', TRUE, 4.4, 250, 600, '10:00', '15:00', 'ул. Зубовский б-р, 20', ARRAY['Открытая кухня', 'Ресторан'], 111),
    ('444785cf-fbbf-4c3a-8f5b-5a5a04b9010a', 55.737400, 37.584920, 'Синяя Птица', 'Ресторан с деловыми обедами и хорошим выбором блюд.', TRUE, 4.8, 800, 1600, '12:00', '15:00', 'ул. Сретенка, 8', ARRAY['Бизнес-ланч', 'Ресторан', 'Открытая кухня', 'ULTIMA GUIDE'], 112),
    ('c91f4b3c-62bd-46c1-88e7-99490ece2f16', 55.739200, 37.592880, 'Lunch Spot', 'Небольшое кафе, предлагающее вкусные и недорогие бизнес-ланчи.', TRUE, 4.6, 600, 1200, '12:00', '14:00', 'ул. Покровка, 20', ARRAY['Завтрак', 'Бизнес-ланч', 'Ресторан', 'Бар'], 113),
    ('8f722a53-dc95-4f5a-a3b6-295523da336f', 55.734300, 37.598150, 'Сладкая Жизнь', 'Кондитерская с большим ассортиментом тортов, пирожных и других сладостей.', TRUE, 4.9, 500, 1500, '09:00', '19:00', 'ул. Волхонка, 14', ARRAY['Семейный ужин с детьми', 'Завтрак'], 114),
    ('69e97576-2e38-4093-aacb-5dd4fdbd7b61', 55.736900, 37.601450, 'Patisserie de Paris', 'Французская кондитерская с изысканными десертами.', TRUE, 4.8, 700, 1800, '08:00', '20:00', 'ул. Арбат, 35', ARRAY['Выпить кофе', 'Бар'], 115),
    ('d4e9a369-a302-4bed-b23a-e5ce66a88d23', 55.739110, 37.595370, 'Чайхона №1', 'Чайная с восточным колоритом, уютными диванами и разнообразными сортами чая.', TRUE, 4.7, 500, 1300, '11:00', '23:00', 'ул. Остоженка, 21', ARRAY['Бизнес-ланч'], 116),
    ('4bcc8cb7-1073-48e8-b8e6-4e7ed208de78', 55.738900, 37.589740, 'АндерСон', 'Кафе для детей с разнообразным меню и игровыми зонами.', TRUE, 4.9, 600, 1500, '09:00', '20:00', 'ул. Льва Толстого, 20', ARRAY['В тренде', 'Открытая кухня'], 117),
    ('01b98306-abc1-4719-9bf3-cf23534af95f', 55.735300, 37.591120, 'Волшебный Замок', 'Детское кафе с тематическими декорациями и веселыми мероприятиями.', TRUE, 4.8, 500, 1400, '10:00', '19:00', 'ул. Пятницкая, 22', ARRAY['Выпить кофе', 'Открытая кухня'], 118);

CREATE INDEX desc_score_idx ON guide.places(score DESC NULLS LAST);

INSERT INTO guide.selections(
             id,
             name,
             description,
             is_collection
)
VALUES
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Рестораны возсле Яндекса', 'Вкусно', FALSE),
    ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Пиццерии возсле Яндекса', 'Быстро', FALSE),
    ('d387e611-05a1-4505-8bf8-9d1486a00f83', 'Кофейни возсе Яндекса', 'Красивый интерьер', FALSE),
    ('91c0a3ee-76b3-4fac-a0f6-247dd1cf5f75', 'Завтраки', 'Европейская кухня', TRUE),
    ('4f06e661-7fed-4713-81c1-9df4ac2d17dd', 'Завтраки', 'Домашняя кухня', TRUE),
    ('85474fba-294c-40ba-9be3-6d0607b71d15', 'Столовые возле Яндекса', 'Недорого', FALSE),
    ('74ba4bb4-5eec-4ba0-8675-161a5dc7e90b', 'Бизнес ланчи', 'Вкусно', FALSE),
    ('9097a7bf-26e0-4099-960a-818713c500f2', 'Кондитерские возле Яндекса', 'Богатый ассортимент', FALSE),
    ('2822cbc7-7b0d-41fb-81a7-ad3f901ca9e5', 'Чайные возсле Яндекса', 'Уютная атмосфера', FALSE),
    ('65609266-5ddb-4496-9638-32011aabf730', 'Детские кафе', 'Вкусно', FALSE)
ON CONFLICT DO NOTHING;


INSERT INTO guide.visibility (
      selection_id,
      user_id
)
VALUES
    ('91c0a3ee-76b3-4fac-a0f6-247dd1cf5f75', '61846daf-3303-465e-ab7f-4af27d3e8f41'),
    ('4f06e661-7fed-4713-81c1-9df4ac2d17dd', '61846daf-3303-465e-ab7f-4af27d3e8f41');


INSERT INTO guide.places_selections(
        place_id,
        selection_id
)
VALUES
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
    ('5794d6b8-931a-492c-a08b-9cf0aba52bcc', 'd387e611-05a1-4505-8bf8-9d1486a00f83'),
    ('41a4a248-21c4-4eb0-8a01-6deeb7207b92', 'd387e611-05a1-4505-8bf8-9d1486a00f83'),
    ('71a12089-32ec-4f3d-a872-c740291524ff', '91c0a3ee-76b3-4fac-a0f6-247dd1cf5f75'),
    ('e0f1e615-ab7f-49a3-9594-27907a91ffd1', '91c0a3ee-76b3-4fac-a0f6-247dd1cf5f75'),
    ('245e864e-f264-4222-aca7-416899098386', '4f06e661-7fed-4713-81c1-9df4ac2d17dd'),
    ('6c7584cc-1933-4183-95a9-6b90f54fb10e', '4f06e661-7fed-4713-81c1-9df4ac2d17dd'),
    ('7e4adcef-25f4-4bcc-ba3c-a743fa8d78a7', '85474fba-294c-40ba-9be3-6d0607b71d15'),
    ('97b3f9cc-93af-4c27-9429-03d476dc38db', '85474fba-294c-40ba-9be3-6d0607b71d15'),
    ('444785cf-fbbf-4c3a-8f5b-5a5a04b9010a', '74ba4bb4-5eec-4ba0-8675-161a5dc7e90b'),
    ('c91f4b3c-62bd-46c1-88e7-99490ece2f16', '74ba4bb4-5eec-4ba0-8675-161a5dc7e90b'),
    ('8f722a53-dc95-4f5a-a3b6-295523da336f', '9097a7bf-26e0-4099-960a-818713c500f2'),
    ('69e97576-2e38-4093-aacb-5dd4fdbd7b61', '9097a7bf-26e0-4099-960a-818713c500f2'),
    ('d4e9a369-a302-4bed-b23a-e5ce66a88d23', '2822cbc7-7b0d-41fb-81a7-ad3f901ca9e5'),
    ('4bcc8cb7-1073-48e8-b8e6-4e7ed208de78', '65609266-5ddb-4496-9638-32011aabf730'),
    ('01b98306-abc1-4719-9bf3-cf23534af95f', '65609266-5ddb-4496-9638-32011aabf730')
ON CONFLICT DO NOTHING;
