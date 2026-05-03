-- Fix database schema for Smart Booking System
-- Run this in phpMyAdmin if you get errors

USE booking_app;

-- Drop existing tables to start fresh
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at DATETIME
);

-- Create services table (using ServiceEntity)
CREATE TABLE services (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    duration INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    image_url VARCHAR(500)
);

-- Create bookings table
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(50) NOT NULL,
    note TEXT,
    created_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (service_id) REFERENCES services(id)
);

-- Insert default admin
INSERT INTO users (name, email, password, role) VALUES
('Administrator', 'admin@booking.com', '$2a$10$encrypted', 'ADMIN');

-- Note: Password will be re-created by the application
-- Just run the table creation part above
