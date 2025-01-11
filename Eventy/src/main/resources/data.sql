-- Pictures
INSERT INTO pictures
    (id, solution_id, path)
VALUES
    (DEFAULT, NULL, 'event_organiser_profile_picture.png'),
    (DEFAULT, NULL, 'solution_provider_profile_picture.png');

-- Users
INSERT INTO users (
    id, user_type, email, password, address, phone_number, is_active, is_deactivated, enabled, has_silenced_notifications, suspension_deadline, role_id, last_password_reset_date, description, first_name, last_name, name
)
VALUES
    (DEFAULT, 'Organizer', 'tac@gmail.com', 'tac', '123 Main St, City, Country', '+3816543', TRUE, FALSE, TRUE, FALSE, NULL, 3, NOW(), 'Description', 'First Name', 'Last Name', 'Admin User'),
    (DEFAULT, 'Provider', 'provider@gmail.com', 'tac', '123 Main St, City, Country', '+3816543', TRUE, FALSE, TRUE, FALSE, NULL, 4, NOW(), 'Description', 'Provider', 'Provideric', 'Admin User');

-- Users Profile Pictures
INSERT INTO public.users_profile_pictures(
    picture_id, user_id)
VALUES (1, 1);
VALUES (2, 2);

-- Locations
INSERT INTO locations(latitude, longitude, id, address, name)
VALUES
    (40.7128, -74.0060, DEFAULT, '123 Main St, New York, NY 10001', 'New York Office'),
    (34.0522, -118.2437, DEFAULT, '456 Sunset Blvd, Los Angeles, CA 90001', 'Los Angeles Office'),
    (51.5074, -0.1278, DEFAULT, '789 Oxford St, London, W1D 2ES', 'London Office');

-- Event Types
INSERT INTO event_types(is_active, id, description, name)
VALUES
    (TRUE, DEFAULT, 'Description for EventType 1', 'EventType 1'),
    (TRUE, DEFAULT, 'Description for EventType 2', 'EventType 2'),
    (FALSE, DEFAULT, 'Description for EventType 3', 'EventType 3');

-- Events
INSERT INTO events(
    max_number_participants, privacy, date,event_type_id, id, location_id, organizer_id, description, name
)
VALUES
    (50, 0, '2024-12-25 15:00:00', 1, DEFAULT, 1, 1, 'Sample Event Description for Event Type 1', 'Sample Event Name for Event Type 1'),
    (100, 0, '2024-12-26 15:00:00', 2, DEFAULT, 2, 1, 'Sample Event Description for Event Type 2', 'Sample Event Name for Event Type 2'),
    (150, 0, '2024-12-27 15:00:00', 3, DEFAULT, 3, 1, 'Sample Event Description for Event Type 3', 'Sample Event Name for Event Type 3');

-- Categories
INSERT INTO categories (id, name, description, status, is_deleted)
VALUES
    (DEFAULT, 'Lighting & Decorations', 'Items for event lighting and venue decoration.', 0, false),
    (DEFAULT, 'Photography & Videography', 'Services for capturing memories of events.', 0, false),
    (DEFAULT, 'Catering', 'Food and drink services for events.', 0, false);

-- Products
INSERT INTO public.solutions(
    discount, is_available, is_deleted, is_visible, price, category_id, id, provider_id, type, description, name)
VALUES
    (0, true, false, true, 50.00, 1, DEFAULT, 2, 'PRODUCT', 'A set of decorative string lights perfect for wedding venues.', 'Decorative String Lights'),
    (15, true, false, true, 30.00, 1, DEFAULT, 2, 'PRODUCT', 'Elegant and comfortable chairs for high-end events.', 'Luxury Event Chairs');

-- Services
INSERT INTO public.solutions(
    discount, is_available, is_deleted, is_visible, price, category_id, id, provider_id, type, description, name)
VALUES
    (0, true, false, true, 500.00, 2, DEFAULT, 2, 'SERVICE', 'Professional photography for weddings, birthdays, and corporate events.', 'Event Photography'),
    (20, true, false, true, 1500.00, 3, DEFAULT, 2, 'SERVICE', 'Full-service catering for events with customizable menus.', 'Catering Service'),
    (10, true, false, true, 800.00, 2, DEFAULT, 2, 'SERVICE', 'Experienced DJ with a wide range of music genres for your event.', 'DJ Service');