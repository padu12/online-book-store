INSERT INTO roles (id, role_name)
VALUES (1, 'ROLE_USER');

INSERT INTO roles (id, role_name)
VALUES (2, 'ROLE_ADMIN');

INSERT INTO categories (id, name, is_deleted)
VALUES (1, 'Fantasy', false);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'Zakhar Berkut', 'Ivan Franko', '9781691430703', 20, 'Its a cool book',
        'https://www.image.com', false);

INSERT INTO books_categories (category_id, book_id)
VALUES (1, 1);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (2, 'Lisova Pisnia', 'Lesia Ukrainka', '9787816949778', 12, 'Its a cool book',
        'https://www.image.com', false);

INSERT INTO books_categories (category_id, book_id)
VALUES (1, 2);

INSERT INTO users (id, email, password, first_name, last_name, shopping_address, is_deleted)
VALUES (1, 'user@example.com', 'your_password', 'John', 'Doe', '123 Main Street', false);

INSERT INTO users (id, email, password, first_name, last_name, shopping_address, is_deleted)
VALUES (2, 'admin@example.com', 'your_password', 'John', 'Doe', '123 Main Street', false);

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1);

INSERT INTO users_roles (user_id, role_id)
VALUES (2, 2);

INSERT INTO orders (id, user_id, status, total, order_date, shipping_address, is_deleted)
VALUES (1, 1, 'PENDING', 220, '2021-12-01 14:30:15', '123 Main Street', false);

INSERT INTO order_items (id, order_id, book_id, quantity, price, is_deleted)
VALUES (1, 1, 1, 5, 100, false);

INSERT INTO order_items (id, order_id, book_id, quantity, price, is_deleted)
VALUES (2, 1, 2, 10, 100, false);
