DROP TABLE IF EXISTS reserves;
DROP TABLE IF EXISTS seats;

CREATE TABLE seats
(
    seat_id     bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    category    VARCHAR(50) NOT NULL,
    seat_status VARCHAR(50) NOT NULL,
    created_at  datetime(6) NOT NULL,
    updated_at  datetime(6) DEFAULT NULL
);

CREATE TABLE reserves
(
    reserve_id     binary(16) PRIMARY KEY,
    student_id     VARCHAR(10) NOT NULL,
    student_name   VARCHAR(50) NOT NULL,
    seat_id        bigint UNIQUE,
    reserve_status VARCHAR(50) NOT NULL,
    created_at     datetime(6) NOT NULL,
    updated_at     datetime(6) DEFAULT NULL,
    CONSTRAINT fk_reserves_to_seats FOREIGN KEY (seat_id) REFERENCES seats (seat_id) ON DELETE CASCADE
);

