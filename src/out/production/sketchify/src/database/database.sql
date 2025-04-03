-- Ensure the database exists
CREATE DATABASE IF NOT EXISTS scribble_db;
USE scribble_db;

-- Create a new `users` table with the correct structure
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL
);
-- Insert default users
INSERT INTO users (username, password) VALUES 
    ('player1', '1234'), 
    ('player2', 'password');
select * from users;


CREATE TABLE words (
    id INT PRIMARY KEY AUTO_INCREMENT,
    word VARCHAR(50) UNIQUE NOT NULL
);

INSERT INTO words (word) VALUES 
('apple'), ('banana'), ('car'), ('house'), ('tree'), ('guitar'), ('mountain'), ('sun'), ('computer'), ('river');
