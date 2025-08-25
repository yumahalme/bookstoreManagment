-- Initialize Roles
INSERT INTO roles (name, description, is_active, created_at, updated_at, version) VALUES
('ROLE_ADMIN', 'Administrator with full access', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('ROLE_USER', 'Regular user with read access', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- Initialize Users (password: password for both admin and user)
INSERT INTO users (username, email, password, first_name, last_name, is_active, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, created_at, updated_at, version) VALUES
('admin', 'admin@bookstore.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Admin', 'User', true, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('user', 'user@bookstore.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Regular', 'User', true, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- Link Users to Roles (using auto-generated user IDs: 1, 2)
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- admin -> ROLE_ADMIN
(2, 2); -- user -> ROLE_USER

-- Initialize Authors (Note: Author entity doesn't have is_active column)
INSERT INTO authors (name, biography, email, country, birth_year, created_at, updated_at, version) VALUES
('J.K. Rowling', 'British author best known for the Harry Potter series', 'jk.rowling@email.com', 'United Kingdom', 1965, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('George R.R. Martin', 'American novelist and short story writer', 'grrm@email.com', 'United States', 1948, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('Stephen King', 'American author of horror, supernatural fiction, and suspense', 'stephen.king@email.com', 'United States', 1947, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('Agatha Christie', 'English writer known for her detective novels', 'agatha.christie@email.com', 'United Kingdom', 1890, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- Initialize Genres
INSERT INTO genres (name, description, category, is_active, created_at, updated_at, version) VALUES
('Fantasy', 'Fictional genre that uses magic and supernatural elements', 'Fiction', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('Science Fiction', 'Genre that deals with futuristic concepts and technology', 'Fiction', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('Horror', 'Genre intended to scare, unsettle, or horrify the audience', 'Fiction', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('Mystery', 'Genre of fiction that deals with the solution of a crime', 'Fiction', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('Romance', 'Genre focusing on love and romantic relationships', 'Fiction', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('Thriller', 'Genre that uses suspense, tension, and excitement', 'Fiction', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- Initialize Books
INSERT INTO books (title, isbn, description, price, stock_quantity, published_year, publisher, language, page_count, is_active, created_at, updated_at, version) VALUES
('Harry Potter and the Philosopher''s Stone', '9780747532699', 'The first novel in the Harry Potter series', 29.99, 50, 1997, 'Bloomsbury', 'English', 223, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('A Game of Thrones', '9780553103540', 'The first novel in A Song of Ice and Fire series', 34.99, 30, 1996, 'Bantam Books', 'English', 694, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('The Shining', '9780385121675', 'A horror novel by Stephen King', 24.99, 25, 1977, 'Doubleday', 'English', 447, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('Murder on the Orient Express', '9780062073495', 'A detective novel featuring Hercule Poirot', 19.99, 40, 1934, 'Collins Crime Club', 'English', 256, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- Link Books to Authors (using auto-generated book IDs: 1, 2, 3, 4)
INSERT INTO book_authors (book_id, author_id) VALUES
(1, 1), -- Harry Potter -> J.K. Rowling
(2, 2), -- A Game of Thrones -> George R.R. Martin
(3, 3), -- The Shining -> Stephen King
(4, 4); -- Murder on the Orient Express -> Agatha Christie

-- Link Books to Genres (using auto-generated book IDs: 1, 2, 3, 4)
INSERT INTO book_genres (book_id, genre_id) VALUES
(1, 1), -- Harry Potter -> Fantasy
(2, 1), -- A Game of Thrones -> Fantasy
(3, 3), -- The Shining -> Horror
(4, 4); -- Murder on the Orient Express -> Mystery

-- Reset sequences to continue from the next available ID
-- Using simple setval statements instead of DO block for better compatibility
-- Each statement is wrapped in error handling to prevent failures

-- Reset roles sequence
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'roles_id_seq') THEN
        PERFORM setval('roles_id_seq', (SELECT MAX(id) FROM roles));
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore sequence errors
END $$;

-- Reset users sequence
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'users_id_seq') THEN
        PERFORM setval('users_id_seq', (SELECT MAX(id) FROM users));
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore sequence errors
END $$;

-- Reset authors sequence
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'authors_id_seq') THEN
        PERFORM setval('authors_id_seq', (SELECT MAX(id) FROM authors));
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore sequence errors
END $$;

-- Reset genres sequence
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'genres_id_seq') THEN
        PERFORM setval('genres_id_seq', (SELECT MAX(id) FROM genres));
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore sequence errors
END $$;

-- Reset books sequence
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'books_id_seq') THEN
        PERFORM setval('books_id_seq', (SELECT MAX(id) FROM books));
    END IF;
EXCEPTION WHEN OTHERS THEN
    -- Ignore sequence errors
END $$;
