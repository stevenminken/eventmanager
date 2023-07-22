-- INSERT INTO artists
INSERT INTO artists (name, genre) VALUES ('Suzan & Freek', 'Nederlands');
INSERT INTO artists (name, genre) VALUES ('The Prodigy', 'techno');
INSERT INTO artists (name, genre) VALUES ('Maan', 'Dutch');
INSERT INTO artists (name, genre) VALUES ('Coldplay', 'populair');
INSERT INTO artists (name, genre) VALUES ('Metalica', 'rock');
INSERT INTO artists (name, genre) VALUES ('Andre Rieu', 'soft classic');
INSERT INTO artists (name, genre) VALUES ('Adele', 'songs');

-- INSERT INTO events
INSERT INTO events (name, date, availability, tickets_sold) VALUES ('Mojo', '2024-03-21', 5000, 0);
INSERT INTO events (name, date, availability, tickets_sold) VALUES ('Pinkpop', '2024-07-08', 8000, 0);
INSERT INTO events (name, date, availability, tickets_sold) VALUES ('Summer Festival', '2025-08-21', 3000, 0);

-- INSERT INTO locations
INSERT INTO locations (name, address, email, number_of_seats) VALUES ('Ahoy', 'Rotterdamstraat 1', 'ahoy@ahoy.nl', 10000);
INSERT INTO locations (name, address, email, number_of_seats) VALUES ('Amsterdam Arena', 'Amsterdamstraat 14', 'arena@arena.nl', 10000);
INSERT INTO locations (name, address, email, number_of_seats) VALUES ('Beursgebouw Eindhoven', 'Stationstraat 4', 'info@beursgebouw.nl', 3000);

-- INSERT INTO users
-- password = "password" (this comment is a security leak, never put plaintext passwords in your code.
-- If you forget your plaintext password here, you have to set a new encrypted password.)
INSERT INTO users (username, password, email, enabled) VALUES ('testuser123', '$2y$10$1qCLmItB.xPIhd2h3MaIDelkS2dAi4COALzgtwjqOpTsYdc22R7cu', 'testuser@test.nl', TRUE);
INSERT INTO users (username, password, email, enabled) VALUES ('testadmin123', '$2y$10$rI7tyYAkN5NVTAGhfm661eN6ms.PQ.jAXpNkzCVdWopV19nhvJ6Ie', 'testadmin@test.nl', TRUE);

-- INSERT INTO authorities
INSERT INTO authorities (username, authority) VALUES ('testuser12', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('testadmin123', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) VALUES ('testadmin123', 'ROLE_USER');
