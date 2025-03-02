-- Вставка жанров, которые связаны с книгами
INSERT INTO genre (name) VALUES ('Uncategorized');
INSERT INTO genre (name, description) VALUES ('Fantasy', 'Books that contain magical or fantastical elements.');
INSERT INTO genre (name, description) VALUES ('Mystery', 'Books involving solving a crime or puzzle.');
INSERT INTO genre (name, description) VALUES ('Romance', 'Books centered around a romantic relationship.');
INSERT INTO genre (name, description) VALUES ('Horror', 'Books designed to scare and unsettle the reader.');


INSERT INTO author (first_name, last_name, pseudonym) VALUES ('George', 'Orwell', 'G. Orwell');
INSERT INTO author (first_name, last_name, pseudonym) VALUES ('J.K.', 'Rowling', 'J.K. Rowling');
INSERT INTO author (first_name, last_name, pseudonym) VALUES ('Arthur', 'Conan Doyle', 'A. Conan Doyle');
INSERT INTO author (first_name, last_name, pseudonym) VALUES ('Jane', 'Austen', 'J. Austen');
INSERT INTO author (first_name, last_name, pseudonym) VALUES ('Stephen', 'King', 'S. King');


INSERT INTO book (title, description) VALUES ('1984', 'A dystopian novel about totalitarianism.');
INSERT INTO book (title, description) VALUES ('Harry Potter and the Sorcerer''s Stone', 'A young boy discovers he is a wizard.');
INSERT INTO book (title, description) VALUES ('The Adventures of Sherlock Holmes', 'A detective solves mysteries in Victorian London.');
INSERT INTO book (title, description) VALUES ('Pride and Prejudice', 'A classic romance novel set in the early 19th century.');
INSERT INTO book (title, description) VALUES ('The Shining', 'A psychological horror novel set in a haunted hotel.');


INSERT INTO book_genre (book_id, genre_id) VALUES (1, 1); -- 1984 - Fantasy
INSERT INTO book_genre (book_id, genre_id) VALUES (2, 1); -- Harry Potter - Fantasy
INSERT INTO book_genre (book_id, genre_id) VALUES (3, 2); -- Sherlock Holmes - Mystery
INSERT INTO book_genre (book_id, genre_id) VALUES (4, 3); -- Pride and Prejudice - Romance
INSERT INTO book_genre (book_id, genre_id) VALUES (5, 4); -- The Shining - Horror


INSERT INTO book_author (book_id, author_id) VALUES (1, 1); -- 1984 - George Orwell
INSERT INTO book_author (book_id, author_id) VALUES (2, 2); -- Harry Potter - J.K. Rowling
INSERT INTO book_author (book_id, author_id) VALUES (3, 3); -- Sherlock Holmes - Arthur Conan Doyle
INSERT INTO book_author (book_id, author_id) VALUES (4, 4); -- Pride and Prejudice - Jane Austen
INSERT INTO book_author (book_id, author_id) VALUES (5, 5); -- The Shining - Stephen King
