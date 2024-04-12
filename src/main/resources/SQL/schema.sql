DROP TABLE IF EXISTS users_banks;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS banks;

-- Создание таблицы "cities"
CREATE TABLE IF NOT EXISTS cities (
    city_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    city_name VARCHAR(100)
);

-- Создание таблицы "banks"
CREATE TABLE IF NOT EXISTS banks (
    bank_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bank_name VARCHAR(100)
);

-- Создание таблицы "users"
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_name VARCHAR(100),
    city_id BIGINT REFERENCES cities(city_id)
);

-- Создание таблицы "users_banks"
CREATE TABLE IF NOT EXISTS users_banks (
    users_banks_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT REFERENCES users(user_id),
    bank_id BIGINT REFERENCES banks(bank_id),
    CONSTRAINT unique_link UNIQUE (user_id, bank_id)
);

-- Наполнение таблицы "cities"
INSERT INTO cities (city_name) VALUES
('Moscow'),
('St. Petersburg'),
('Novgorod');

-- Наполнение таблицы "banks"
INSERT INTO banks (bank_name) VALUES
('Sberbank'),
('AlfaBank'),
('VTB bank'),
('Tinkoff bank');

-- Наполнение таблицы "users"
INSERT INTO users (user_name, city_id) VALUES
('Ivan Ivanov', 1),
('Alexandr Romanov', 2),
('Kirill Petrov', 3);

-- Наполнение таблицы "users_banks"
INSERT INTO users_banks (user_id, bank_id) VALUES
(1, 1), -- Alice в Chase Bank
(2, 2), -- Bob в Bank of America
(3, 3), -- Charlie в Wells Fargo
(1, 2), -- Alice также в Bank of America
(2, 4); -- Alice также в Bank of America