-- Pictures
INSERT INTO pictures
    (id, solution_id, path)
VALUES
    (1, NULL, 'event_organiser_profile_picture.png');

-- Users
INSERT INTO users (
    id, user_type, email, password, address, phone_number, is_active, is_deactivated, enabled, has_silenced_notifications, suspension_deadline, role_id, last_password_reset_date, description, first_name, last_name, name
)
VALUES
    (DEFAULT, 'Organizer', 'tac@gmail.com', 'tac', '123 Main St, City, Country', '+3816543', TRUE, FALSE, TRUE, FALSE, NULL, 3, NOW(), 'Description', 'First Name', 'Last Name', 'Admin User');

-- Users Profile Pictures
INSERT INTO public.users_profile_pictures(
    picture_id, user_id)
VALUES (1, 1);

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
