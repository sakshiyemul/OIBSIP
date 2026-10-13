CREATE DATABASE IF NOT EXISTS oibsip_reservation CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE oibsip_reservation;

CREATE TABLE IF NOT EXISTS app_users (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 username VARCHAR(50) NOT NULL UNIQUE,
 password_hash VARCHAR(64) NOT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS trains (
 train_number INT PRIMARY KEY,
 train_name VARCHAR(100) NOT NULL,
 source_station VARCHAR(100) NOT NULL,
 destination_station VARCHAR(100) NOT NULL
);
CREATE TABLE IF NOT EXISTS reservations (
 pnr BIGINT PRIMARY KEY AUTO_INCREMENT,
 username VARCHAR(50) NOT NULL,
 passenger_name VARCHAR(100) NOT NULL,
 train_number INT NOT NULL,
 travel_class VARCHAR(30) NOT NULL,
 journey_date DATE NOT NULL,
 source_station VARCHAR(100) NOT NULL,
 destination_station VARCHAR(100) NOT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_res_train FOREIGN KEY (train_number) REFERENCES trains(train_number),
 CONSTRAINT fk_res_user FOREIGN KEY (username) REFERENCES app_users(username)
);
INSERT INTO trains (train_number, train_name, source_station, destination_station) VALUES
(12123, 'Deccan Express', 'Pune', 'Mumbai'),
(11029, 'Koyna Express', 'Mumbai', 'Kolhapur'),
(12101, 'Jnaneswari Express', 'Mumbai', 'Howrah'),
(12025, 'Shatabdi Express', 'Pune', 'Hyderabad')
ON DUPLICATE KEY UPDATE train_name = VALUES(train_name);
