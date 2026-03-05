-- ============================================================
-- Ocean View Resort - Database Schema
-- Database: oceanview_db
-- ============================================================

CREATE DATABASE IF NOT EXISTS oceanview_db;
USE oceanview_db;

-- ============================================================
-- USERS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    phone VARCHAR(20),
    role ENUM('SUPERADMIN', 'ADMIN', 'USER') NOT NULL DEFAULT 'USER',
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    verification_token VARCHAR(255),
    reset_token VARCHAR(255),
    reset_token_expiry DATETIME,
    remember_token VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_username (username),
    INDEX idx_role (role),
    INDEX idx_verification_token (verification_token),
    INDEX idx_reset_token (reset_token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- ROOMS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(10) NOT NULL UNIQUE,
    room_type ENUM('STANDARD', 'DELUXE', 'SUITE', 'PRESIDENTIAL') NOT NULL,
    price_per_night DECIMAL(10,2) NOT NULL,
    capacity INT NOT NULL DEFAULT 2,
    status ENUM('AVAILABLE', 'OCCUPIED', 'MAINTENANCE') NOT NULL DEFAULT 'AVAILABLE',
    description TEXT,
    image_url VARCHAR(255),
    floor INT,
    has_ocean_view BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_room_type (room_type),
    INDEX idx_status (status),
    INDEX idx_room_number (room_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- RESERVATIONS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    reservation_number VARCHAR(20) NOT NULL UNIQUE,
    user_id INT NOT NULL,
    guest_name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    contact_number VARCHAR(20) NOT NULL,
    room_id INT,
    room_type ENUM('STANDARD', 'DELUXE', 'SUITE', 'PRESIDENTIAL') NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    num_guests INT DEFAULT 1,
    special_requests TEXT,
    total_amount DECIMAL(10,2),
    status ENUM('PENDING', 'CONFIRMED', 'CHECKED_IN', 'CHECKED_OUT', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE SET NULL,
    INDEX idx_reservation_number (reservation_number),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_check_in (check_in),
    INDEX idx_check_out (check_out),
    INDEX idx_room_type (room_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- PAYMENTS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    reservation_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    tax DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    service_charge DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total DECIMAL(10,2) NOT NULL,
    payment_method ENUM('CASH', 'CARD', 'ONLINE') DEFAULT 'CASH',
    payment_status ENUM('PENDING', 'COMPLETED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE,
    INDEX idx_reservation_id (reservation_id),
    INDEX idx_payment_status (payment_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TRIGGER: Auto-generate reservation number
-- Format: OVR-YYYYMMDD-XXXX (e.g., OVR-20260218-0001)
-- ============================================================
DELIMITER //
CREATE TRIGGER IF NOT EXISTS before_reservation_insert
BEFORE INSERT ON reservations
FOR EACH ROW
BEGIN
    DECLARE next_num INT;
    DECLARE today_str VARCHAR(8);
    
    SET today_str = DATE_FORMAT(NOW(), '%Y%m%d');
    
    SELECT COALESCE(MAX(
        CAST(SUBSTRING(reservation_number, 14) AS UNSIGNED)
    ), 0) + 1 INTO next_num
    FROM reservations
    WHERE reservation_number LIKE CONCAT('OVR-', today_str, '-%');
    
    IF NEW.reservation_number IS NULL OR NEW.reservation_number = '' THEN
        SET NEW.reservation_number = CONCAT('OVR-', today_str, '-', LPAD(next_num, 4, '0'));
    END IF;
END //
DELIMITER ;

-- ============================================================
-- SEED DATA
-- ============================================================

-- SuperAdmin account (password: Admin@123 - BCrypt hashed)
INSERT IGNORE INTO users (username, email, password_hash, full_name, role, is_verified) VALUES
('superadmin', 'superadmin@oceanview.com', '$2a$12$LJ3m4ys3LzSodX3Q/MIJaOJ3fFqSINoz9P8DCXE3CjXhl2bRhEzHe', 'System Administrator', 'SUPERADMIN', TRUE),
('admin', 'admin@oceanview.com', '$2a$12$LJ3m4ys3LzSodX3Q/MIJaOJ3fFqSINoz9P8DCXE3CjXhl2bRhEzHe', 'Hotel Manager', 'ADMIN', TRUE);

-- Room Types with pricing
INSERT IGNORE INTO rooms (room_number, room_type, price_per_night, capacity, status, description, floor, has_ocean_view) VALUES
('101', 'STANDARD', 75.00, 2, 'AVAILABLE', 'Comfortable standard room with modern amenities, queen-size bed, and garden view.', 1, FALSE),
('102', 'STANDARD', 75.00, 2, 'AVAILABLE', 'Cozy standard room with modern furnishings and garden view.', 1, FALSE),
('103', 'STANDARD', 85.00, 2, 'AVAILABLE', 'Standard room with partial ocean view and balcony.', 1, TRUE),
('201', 'DELUXE', 150.00, 3, 'AVAILABLE', 'Spacious deluxe room with king-size bed, ocean view, and private balcony.', 2, TRUE),
('202', 'DELUXE', 150.00, 3, 'AVAILABLE', 'Elegant deluxe room featuring panoramic ocean views and luxury bath.', 2, TRUE),
('203', 'DELUXE', 140.00, 2, 'AVAILABLE', 'Deluxe room with garden view, premium linens, and mini-bar.', 2, FALSE),
('301', 'SUITE', 280.00, 4, 'AVAILABLE', 'Luxurious suite with separate living area, oceanfront view, jacuzzi, and premium amenities.', 3, TRUE),
('302', 'SUITE', 280.00, 4, 'AVAILABLE', 'Premium suite with wraparound balcony, dining area, and butler service.', 3, TRUE),
('401', 'PRESIDENTIAL', 500.00, 6, 'AVAILABLE', 'The ultimate luxury - presidential suite with private terrace, infinity pool, personal chef, and 24/7 butler.', 4, TRUE),
('402', 'PRESIDENTIAL', 500.00, 6, 'AVAILABLE', 'Exclusive presidential suite with panoramic ocean views, private dining, and VIP concierge.', 4, TRUE);

-- ============================================================
-- END OF SCHEMA
-- ============================================================
