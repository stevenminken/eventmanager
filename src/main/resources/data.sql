INSERT INTO artists (name, genre)
VALUES ('jan', 'rock'),
       ('Piet', 'rap');

INSERT INTO events (name, date, availability, tickets_sold)
VALUES ('Metallica', '2019-01-21', 2000, 0),
       ('Andre Rieu', '2019-01-21', 4000, 0),
       ('Adele', '2019-01-21', 3000, 0);


INSERT INTO locations (name, address, email, number_of_seats)
VALUES ('Ahoy', 'Rotterdamstraat 1', 'ahoy@ahoy.nl', 5000),
       ('Amsterdam Arena', 'Amsterdamstraat 14', 'arena@arena.nl', 7000),
       ('Beursgebouw Eindhoven', 'Stationstraat 4', 'info@beursgebouw.nl', 2500);

-- password = "password" (dit comment is een security lek, zet dit nooit in je code.
-- Als je hier je plaintext password niet meer weet, moet je een nieuw password encrypted)
INSERT INTO users (username, password, email, enabled)
VALUES ('user', '$2y$10$1qCLmItB.xPIhd2h3MaIDelkS2dAi4COALzgtwjqOpTsYdc22R7cu', 'user@test.nl', TRUE);
INSERT INTO users (username, password, email, enabled)
VALUES ('admin', '$2y$10$rI7tyYAkN5NVTAGhfm661eN6ms.PQ.jAXpNkzCVdWopV19nhvJ6Ie', 'admin@test.nl', TRUE);

INSERT INTO authorities (username, authority)
VALUES ('user', 'ROLE_USER');
INSERT INTO authorities (username, authority)
VALUES ('admin', 'ROLE_USER');
INSERT INTO authorities (username, authority)
VALUES ('admin', 'ROLE_ADMIN');
