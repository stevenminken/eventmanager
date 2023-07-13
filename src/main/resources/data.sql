INSERT INTO artists (name, genre) VALUES
('jan', 'rock'),
('Piet', 'rap');

INSERT INTO events (name, date, availability, tickets_sold) VALUES
('Metallica', '2019-01-21', 2000, 0),
('Andre Rieu', '2019-01-21', 4000, 0),
('Adele', '2019-01-21', 3000, 0);


INSERT INTO locations (name, address, email, number_of_seats) VALUES
('Ahoy', 'Rotterdamstraat 1', 'ahoy@ahoy.nl', 5000),
('Amsterdam Arena', 'Amsterdamstraat 14', 'arena@arena.nl', 7000),
('Beursgebouw Eindhoven', 'Stationstraat 4', 'info@beursgebouw.nl', 2500);

-- password = "password" (dit comment is een security lek, zet dit nooit in je code.
-- Als je hier je plaintext password niet meer weet, moet je een nieuw password encrypted)
INSERT INTO users (username, password, email, enabled) VALUES ('user', '$2a$12$IzA1Ja1LH4PSMoro9PeITO1etDlknPjSX1nLusgt1vi9c1uaEXdEK','user@test.nl', TRUE);
INSERT INTO users (username, password, email, enabled) VALUES ('admin', '$2a$12$IzA1Ja1LH4PSMoro9PeITO1etDlknPjSX1nLusgt1vi9c1uaEXdEK', 'admin@test.nl', TRUE);

INSERT INTO authorities (username, authority) VALUES ('user', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');
