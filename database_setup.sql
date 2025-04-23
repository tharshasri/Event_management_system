CREATE DATABASE event_management;

USE event_management;

CREATE TABLE organizers (
    id VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE audiences (
    phone_number VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL
);

CREATE TABLE events (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    venue VARCHAR(200) NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    max_seats INT NOT NULL,
    ticket_price DOUBLE NOT NULL,
    organizer_id VARCHAR(50) NOT NULL,
    tickets_booked INT DEFAULT 0,
    attendance INT DEFAULT 0,
    FOREIGN KEY (organizer_id) REFERENCES organizers(id)
);

CREATE TABLE bookings (
    id VARCHAR(50) PRIMARY KEY,
    event_id VARCHAR(50) NOT NULL,
    audience_id VARCHAR(20) NOT NULL,
    FOREIGN KEY (event_id) REFERENCES events(id),
    FOREIGN KEY (audience_id) REFERENCES audiences(phone_number)
);

CREATE TABLE budget (
    id VARCHAR(50) PRIMARY KEY,
    organizer_id VARCHAR(50) NOT NULL,
    total_budget DOUBLE NOT NULL,
    FOREIGN KEY (organizer_id) REFERENCES organizers(id)
);

CREATE TABLE attendance (
    booking_id VARCHAR(50) PRIMARY KEY,
    event_id VARCHAR(50) NOT NULL,
    attended BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (booking_id) REFERENCES bookings(id),
    FOREIGN KEY (event_id) REFERENCES events(id)
);