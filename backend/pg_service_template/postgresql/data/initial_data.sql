
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
