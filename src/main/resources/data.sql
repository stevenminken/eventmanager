-- INSERT INTO artists
INSERT INTO artists (name, genre) VALUES ('Suzan & Freek', 'Nederlands');
INSERT INTO artists (name, genre) VALUES ('The Prodigy', 'techno');
INSERT INTO artists (name, genre) VALUES ('Maan', 'Dutch');
INSERT INTO artists (name, genre) VALUES ('Coldplay', 'populair');
INSERT INTO artists (name, genre) VALUES ('Metalica', 'rock');
INSERT INTO artists (name, genre) VALUES ('Andre Rieu', 'soft classic');
INSERT INTO artists (name, genre) VALUES ('Adele', 'songs');
INSERT INTO artists (name, genre) VALUES ('VERKEERDETESTENSTEVEN', 'VERKEERDETESTENSTEVEN');

-- INSERT INTO events
INSERT INTO events (name, date, availability, tickets_sold, document_data) VALUES ('Mojo', '2024-03-21', 5000, 0, null);
INSERT INTO events (name, date, availability, tickets_sold, document_data) VALUES ('Pinkpop', '2024-07-08', 8000, 0, null);
INSERT INTO events (name, date, availability, tickets_sold, document_data) VALUES ('Summer Festival', '2025-08-21', 3000, 0, null);

-- INSERT INTO locations
INSERT INTO locations (name, address, email, number_of_seats) VALUES ('Ahoy', 'Rotterdamstraat 1', 'ahoy@ahoy.nl', 10000);
INSERT INTO locations (name, address, email, number_of_seats) VALUES ('Amsterdam Arena', 'Amsterdamstraat 14', 'arena@arena.nl', 10000);
INSERT INTO locations (name, address, email, number_of_seats) VALUES ('Beursgebouw Eindhoven', 'Stationstraat 4', 'info@beursgebouw.nl', 3000);

-- INSERT INTO users
INSERT INTO users (username, password, email, enabled) VALUES ('user', '$2a$12$HzUJQ3dRUnuXEXC9i9Xt9eYlL7j8aQtoac7kyRaYhrP5qztCYhZGy', 'user@test.nl', TRUE);
INSERT INTO users (username, password, email, enabled) VALUES ('admin', '$2a$12$Tr95ewgVAkvCfjYsH/HAX.UiumqlsT/jkzsKNrP1aw4vO/iuJ.j9C', 'admin@test.nl', TRUE);

-- INSERT INTO authorities
INSERT INTO authorities (username, authority) VALUES ('user', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_USER');
