INSERT INTO role(id, name)
VALUES
    (1, 'ROLE_Admin'),
    (2, 'ROLE_AuthenticatedUser'),
    (3, 'ROLE_Organizer'),
    (4, 'ROLE_Provider');

-- Users
INSERT INTO users (
    id, user_type, email, password, address, phone_number, is_active, is_deactivated, enabled, has_silenced_notifications, suspension_deadline, role_id, last_password_reset_date, last_read_notifications, description, first_name, last_name, name
)
VALUES
    (DEFAULT, 'Organizer', 'tac@gmail.com', '$2a$10$reTt1CDO6L.cknTdczA.c.N/Xjqbt21RiqbYgqobmGSdE1t7cmPVe', '123 Main St, City, Country', '+3816543', TRUE, FALSE, TRUE, FALSE, NULL, 3, NOW(), '2023-10-30 00:00:00', NULL, 'Tac Tac', 'Jezickovic', NULL),
    (DEFAULT, 'Organizer', 'ves@gmail.com', '$2a$10$reTt1CDO6L.cknTdczA.c.N/Xjqbt21RiqbYgqobmGSdE1t7cmPVe', '123 Main St, City, Country', '+3815361', TRUE, FALSE, TRUE, FALSE, NULL, 3, NOW(), '2024-10-30 00:00:00', NULL, 'Ves Ves', 'Jezickovic', NULL),
    (DEFAULT, 'Provider', 'provider@gmail.com', '$2a$10$reTt1CDO6L.cknTdczA.c.N/Xjqbt21RiqbYgqobmGSdE1t7cmPVe', '123 Main St, City, Country', '+3816543', TRUE, FALSE, TRUE, FALSE, NULL, 4, NOW(), '2000-01-01 00:00:00', 'Description', NULL, NULL, 'VIT DOO'),
    (DEFAULT, 'Provider', 'provider2@gmail.com', '$2a$10$reTt1CDO6L.cknTdczA.c.N/Xjqbt21RiqbYgqobmGSdE1t7cmPVe', '123 Main St, City, Country', '+3816543', TRUE, FALSE, TRUE, FALSE, NULL, 4, NOW(), '2000-01-01 00:00:00', 'Description', NULL, NULL, 'Lidl'),
    (DEFAULT, 'Admin', 'admin@gmail.com', '$2a$10$reTt1CDO6L.cknTdczA.c.N/Xjqbt21RiqbYgqobmGSdE1t7cmPVe', 'Cara Dusana 72, Novi Sad', '+381 65 31 43 240', TRUE, FALSE, TRUE, FALSE, NULL, 1, NOW(), '2000-01-01 00:00:00', NULL, 'Veselin', 'Roganovic', NULL),
    (DEFAULT, 'AuthenticatedUser', 'auth@gmail.com', '$2a$10$reTt1CDO6L.cknTdczA.c.N/Xjqbt21RiqbYgqobmGSdE1t7cmPVe', '123 Main St, City, Country', '+3816543', TRUE, FALSE, TRUE, FALSE, NULL, 2, NOW(), '2000-01-01 00:00:00', NULL, NULL, NULL, NULL),
    (DEFAULT, 'AuthenticatedUser', 'c.tamara333@gmail.com', '$2a$10$reTt1CDO6L.cknTdczA.c.N/Xjqbt21RiqbYgqobmGSdE1t7cmPVe', '123 Main St, City, Country', '+3816543', TRUE, FALSE, TRUE, FALSE, NULL, 2, NOW(), '2000-01-01 00:00:00', NULL, NULL, NULL, NULL);

-- Locations
INSERT INTO locations(latitude, longitude, id, address, name)
VALUES
    (40.7128, -74.0060, DEFAULT, '123 Main St, New York, NY 10001', 'New York Office'),
    (34.0522, -118.2437, DEFAULT, '456 Sunset Blvd, Los Angeles, CA 90001', 'Los Angeles Office'),
    (51.5074, -0.1278, DEFAULT, '789 Oxford St, London, W1D 2ES', 'London Office');

-- Event Types
INSERT INTO event_types(is_active, id, description, name)
VALUES
    (TRUE, DEFAULT, 'You know what we want? A HUGE PARTY!', 'Party'),
    (TRUE, DEFAULT, 'Graduations of all types!', 'Graduation'),
    (FALSE, DEFAULT, 'Only for luxurious event', 'Luxury'),
    (TRUE, DEFAULT, 'P U M P A J', 'Workout'),
    (TRUE, DEFAULT, 'All tech events, let`s rock TOGETHER!!', 'Tech');

-- Events
INSERT INTO events(
    max_number_participants, privacy, date, event_type_id, id, location_id, organizer_id, description, name
)
VALUES
    (50, 'PUBLIC', '2024-12-25 15:00:00', 1, DEFAULT, 1, 1, 'This is a birthday party for our friend John, I hope he does not know we are organizing this!', 'Birthday Party for John'),
    (100, 'PUBLIC', '2024-12-26 15:00:00', 2, DEFAULT, 2, 1, 'High school students of prestigious high school "Jonny Smith" are graduating this month!', 'High School Graduation'),
    (150, 'PUBLIC', '2024-12-27 15:00:00', 1, DEFAULT, 3, 2, 'Lunch in our best office ever! See you there! Big thanks to Eventy for being such a cool platform! #sponsored', 'Office Lunch');

-- Accepted Events
INSERT INTO users_attending_events (event_id, user_id) VALUES
(1, 2),
(2, 2),
(1, 7);

-- Categories
INSERT INTO categories (id, name, description, status, is_deleted)
VALUES
    (DEFAULT, 'Decorations', 'Items for event lighting and venue decoration.', 0, false),
    (DEFAULT, 'Photography', 'Services for capturing memories of events.', 0, false),
    (DEFAULT, 'Catering', 'Food and drink services for events.', 0, false);

-- Solutions
INSERT INTO solutions (
    cancellation_deadline, discount, is_available, is_deleted, is_visible, max_reservation_time, min_reservation_time, price,
    reservation_deadline, reservation_type, category_id, id, product_history_id, provider_id, service_history_id, type, description, name, specifics)
VALUES
    -- Products
    (NULL, 10, TRUE, FALSE, TRUE, NULL, NULL, 19.99, NULL, NULL, 1, DEFAULT, NULL, 3, NULL, 'Product', 'High-quality gym dumbbell', 'Dumbbell', NULL),
    (NULL, 5, FALSE, FALSE, TRUE, NULL, NULL, 49.99, NULL, NULL, 2, DEFAULT, NULL, 4, NULL, 'Product', 'Ergonomic office chair', 'Office Chair', NULL),
    (NULL, 15, TRUE, FALSE, TRUE, NULL, NULL, 29.99, NULL, NULL, 3, DEFAULT, NULL, 3, NULL, 'Product', 'Stylish table lamp', 'Table Lamp', NULL),
    (NULL, 20, TRUE, FALSE, TRUE, NULL, NULL, 99.99, NULL, NULL, 1, DEFAULT, NULL, 4, NULL, 'Product', 'Gaming keyboard with RGB', 'Gaming Keyboard', NULL),

    -- Services
    (15, 15, FALSE, FALSE, TRUE, 240, 60, 99.99, 7, 0, 2, DEFAULT, NULL, 4, NULL, 'Service', 'Wedding photography package', 'Photography', 'Includes editing and delivery in 2 weeks'),
    (12, 20, TRUE, FALSE, TRUE, 180, 30, 149.99, 0, 1, 3, DEFAULT, NULL, 4, NULL, 'Service', 'Personal training session', 'Training', 'One-on-one session with certified trainer'),
    (7, 10, TRUE, FALSE, TRUE, 300, 90, 199.99, 14, 1, 2, DEFAULT, NULL, 4, NULL, 'Service', 'Event planning service', 'Event Planning', 'Full-service planning and coordination'),
    (17, 25, TRUE, FALSE, TRUE, 120, 30, 79.99, 14, 0, 3, DEFAULT, NULL, 3, NULL, 'Service', 'House cleaning service', 'Cleaning', 'Deep cleaning for apartments and houses');

-- Pictures
INSERT INTO pictures
(id, solution_id, path)
VALUES
    (DEFAULT, NULL, 'event_organiser_profile_picture.png'),
    (DEFAULT, NULL, 'solution_provider_profile_picture.png'),
    (DEFAULT, NULL, 'admin.png'),
    (DEFAULT, NULL, 'lidl.png'),
    (DEFAULT, 1, 'dumbbell.jpg'),
    (DEFAULT, 2, 'office_chair.webp'),
    (DEFAULT, 3, 'table_lamp.webp'),
    (DEFAULT, 4, 'keyboard.webp'),
    (DEFAULT, 5, 'photography.webp'),
    (DEFAULT, 6, 'training.webp'),
    (DEFAULT, 7, 'event_planning.webp'),
    (DEFAULT, 8, 'cleaning.webp');

-- Users Profile Pictures
INSERT INTO users_profile_pictures(picture_id, user_id)
VALUES (1, 1),
       (2, 2),
       (4, 3),
       (2, 4),
       (3, 5),
       (5, 6),
       (6, 7);

-- Suggested Event Types for Solutions
INSERT INTO suggested_event_types(solution_id, event_type_id)
VALUES (1, 4), -- Dumbbell suggested for Workout
       (1, 5), -- Dumbbell suggested for Tech
       (2, 2), -- Office Chair suggested for Graduation
       (3, 1), -- Table Lamp suggested for Party
       (3, 3), -- Table Lamp suggested for Luxury
       (3, 5), -- Table Lamp suggested for Tech
       (4, 5), -- Gaming Keyboard suggested for Tech
       (5, 1), -- Photography package suggested for Party
       (5, 2), -- Photography package suggested for Graduation
       (5, 3), -- Photography package suggested for Luxury
       (6, 4), -- Personal Training suggested for Workout
       (7, 1), -- Event Planning suggested for Party
       (7, 2), -- Event Planning suggested for Graduation
       (7, 3), -- Event Planning suggested for Luxury
       (8, 1); -- House Cleaning suggested for Party

-- Notifications
INSERT INTO notifications (id, type, redirection_id, title, message, grader_id, grade, timestamp)
VALUES
       -- 1
       (DEFAULT, 'EVENT_CHANGE', 101, 'Event Updated', 'The event "Music Fest" has been updated.', 1, NULL, '2023-11-01 00:00:00'),
       (DEFAULT, 'EVENT_CHANGE', 101, 'Event Updated', 'The event "Music Fest" has been updated.', 1, NULL, '2023-11-01 00:00:00'),
       (DEFAULT, 'EVENT_CHANGE', 101, 'Event Updated', 'The event "Music Fest" has been updated.', 1, NULL, '2022-11-01 00:00:00'),
       (DEFAULT, 'EVENT_CHANGE', 101, 'Event Updated', 'The event "Music Fest" has been updated.', 1, NULL, '2022-11-01 00:00:00'),
       (DEFAULT, 'EVENT_CHANGE', 101, 'Event Updated', 'The event "Music Fest" has been updated.', 1, NULL, '2022-11-01 00:00:00'),
       (DEFAULT, 'EVENT_CHANGE', 101, 'Event Updated', 'The event "Music Fest" has been updated.', 1, NULL, '2022-11-01 00:00:00'),
       (DEFAULT, 'RATING_EVENT', 201, 'New Event Rating', 'Your event "Charity Run" received a new rating.', 3, 5, '2022-11-03 00:00:00');

-- Assign Notifications to Users
INSERT INTO user_notifications (user_id, notification_id) VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4),
    (1, 5),
    (1, 6),
    (1, 7);

