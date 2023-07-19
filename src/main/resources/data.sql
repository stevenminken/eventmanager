INSERT INTO artists (name, genre)
VALUES ('Suzan & Freek', 'Nederlands'),
       ('The Prodigy', 'techno'),
        ('Maan', 'Dutch'),
        ('Coldplay', 'populair'),
        ('Metalica', 'rock'),
        ('Andre Rieu', 'soft classic'),
        ('Adele', 'songs');

INSERT INTO events (name, date, availability, tickets_sold)
VALUES ('Mojo', '2024-03-21', 5000, 0),
       ('Pinkpop', '2024-07-08', 8000, 0),
       ('Summer Festival', '2025-08-21', 3000, 0);


INSERT INTO locations (name, address, email, number_of_seats)
VALUES ('Ahoy', 'Rotterdamstraat 1', 'ahoy@ahoy.nl', 10000),
       ('Amsterdam Arena', 'Amsterdamstraat 14', 'arena@arena.nl', 10000),
       ('Beursgebouw Eindhoven', 'Stationstraat 4', 'info@beursgebouw.nl', 3000);

-- password = "password" (dit comment is een security lek, zet dit nooit in je code.
-- Als je hier je plaintext password niet meer weet, moet je een nieuw password encrypted)
INSERT INTO users (username, password, email, enabled)
VALUES ('user', '$2y$10$1qCLmItB.xPIhd2h3MaIDelkS2dAi4COALzgtwjqOpTsYdc22R7cu', 'user@test.nl', TRUE),
        ('admin', '$2y$10$rI7tyYAkN5NVTAGhfm661eN6ms.PQ.jAXpNkzCVdWopV19nhvJ6Ie', 'admin@test.nl', TRUE);

INSERT INTO authorities (username, authority)
VALUES ('user', 'ROLE_USER'),
        ('admin', 'ROLE_ADMIN');
