create sequence seq_user start with 1000;
grant select, usage on seq_user to postgres;
create table sec_user
(
    id                BIGINT         NOT NULL PRIMARY KEY,
    first_name        VARCHAR(255)   NOT NULL,
    last_name         VARCHAR(255)   NOT NULL,
    user_type         VARCHAR(255)   NOT NULL,
    status            VARCHAR(255)   NOT NULL,
    email             VARCHAR(255)   NOT NULL,
    registration_date TIMESTAMP(255) NOT NULL,
    mobile_number     VARCHAR(255)   NOT NULL,
    password          VARCHAR(255)   NOT NULL,
    role              VARCHAR(255)   NOT NULL,
    country           VARCHAR(255)   NOT NULL,
    image             VARCHAR(4000),
    CONSTRAINT unique_email UNIQUE (email),
    CONSTRAINT unique_mobile_number UNIQUE (mobile_number)
);
grant select, insert, update on sec_user to postgres;

create sequence seq_token start with 1000;
grant select, usage on seq_token to postgres;
create table token
(
    id         BIGINT       NOT NULL PRIMARY KEY,
    token      VARCHAR(255) NOT NULL UNIQUE,
    token_type VARCHAR(255) NOT NULL,
    revoked    BOOLEAN      NOT NULL,
    expired    BOOLEAN      NOT NULL,
    user_id    BIGINT,
    FOREIGN KEY (user_id) REFERENCES sec_user (id),
    CONSTRAINT unique_token UNIQUE (token)
);
grant select, insert, update on token to postgres;

create sequence seq_hotel start with 1000;
grant select, usage on seq_hotel to postgres;
create table hotel
(
    id               BIGINT        NOT NULL PRIMARY KEY,
    name             VARCHAR(255)  NOT NULL,
    address          VARCHAR(255)  NOT NULL,
    city             VARCHAR(255)  NOT NULL,
    zip_code         VARCHAR(255)  NOT NULL,
    phone            VARCHAR(255)  NOT NULL,
    email            VARCHAR(255)  NOT NULL,
    description      VARCHAR(4000) NOT NULL,
    image            VARCHAR(4000),
    price_range_from NUMERIC,
    price_range_to   NUMERIC,
    sec_user_id      BIGINT        NOT NULL,
    FOREIGN KEY (sec_user_id) REFERENCES sec_user (id),
    CONSTRAINT unique_hotel_email UNIQUE (email),
    CONSTRAINT unique_hotel_phone UNIQUE (phone)
);
grant select, insert, update, delete on hotel to postgres;

create sequence seq_hotel_image start with 1000;
grant select, usage on seq_hotel_image to postgres;
CREATE TABLE hotel_image
(
    id        BIGINT       NOT NULL PRIMARY KEY,
    hotel_id  BIGINT       NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (hotel_id) REFERENCES hotel (id)
);
grant select, insert, update, delete on hotel_image to postgres;

create sequence seq_room start with 1000;
grant select, usage on seq_room to postgres;
create table room
(
    id              BIGINT         NOT NULL PRIMARY KEY,
    number          VARCHAR(50)    NOT NULL,
    floor           INT            NOT NULL,
    bed             INT            NOT NULL,
    price_per_night NUMERIC(10, 2) NOT NULL,
    hotel_id        BIGINT         NOT NULL,
    is_reserved     BOOLEAN        NOT NULL,
    image           VARCHAR(4000),
    reserved_from   TIMESTAMP,
    reserved_to     TIMESTAMP,
    reserved_by     BIGINT,
    FOREIGN KEY (reserved_by) REFERENCES sec_user (id),
    FOREIGN KEY (hotel_id) REFERENCES hotel (id),
    CONSTRAINT unique_room_number_hotel_id UNIQUE (number, hotel_id)
);
grant select, insert, update, delete on room to postgres;

create sequence seq_room_image start with 1000;
grant select, usage on seq_room_image to postgres;
CREATE TABLE room_image
(
    id        BIGINT       NOT NULL PRIMARY KEY,
    room_id  BIGINT       NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room (id)
);
grant select, insert, update, delete on room_image to postgres;

create sequence seq_tour start with 1000;
grant select, usage on seq_tour to postgres;
create table tour
(
    id             BIGINT         NOT NULL PRIMARY KEY,
    name           VARCHAR(50)    NOT NULL,
    description    VARCHAR(4000)  NOT NULL,
    price          NUMERIC(10, 2) NOT NULL,
    start_Date     TIMESTAMP      NOT NULL,
    end_Date       TIMESTAMP      NOT NULL,
    total_seats    INT            NOT NULL,
    reserved_seats INT            NOT NULL DEFAULT 0,
    is_active      BOOLEAN        NOT NULL,
    image          VARCHAR(4000),
    CONSTRAINT unique_name UNIQUE (name)
);
grant select, insert, update, delete on tour to postgres;


create sequence seq_tour_image start with 1000;
grant select, usage on seq_tour_image to postgres;
CREATE TABLE tour_image
(
    id          BIGINT       NOT NULL PRIMARY KEY,
    tour_id     BIGINT       NOT NULL,
    image_url   VARCHAR(255) NOT NULL,
    FOREIGN KEY (tour_id) REFERENCES tour (id)
);
grant select, insert, update, delete on tour_image to postgres;

create sequence seq_tour_reserve_user start with 1000;
grant select, usage on seq_tour_reserve_user to postgres;
create table tour_reserve_user
(
    id          BIGINT NOT NULL PRIMARY KEY,
    tour_id     BIGINT NOT NULL,
    sec_user_id BIGINT NOT NULL,
    FOREIGN KEY (tour_id) REFERENCES tour (id),
    FOREIGN KEY (sec_user_id) REFERENCES sec_user (id)
);
grant select, insert, update, delete on tour_reserve_user to postgres;

create sequence seq_hotel_rating start with 1000;
grant select, usage on sequence seq_hotel_rating to postgres;
create table hotel_rating
(
    id          BIGINT NOT NULL PRIMARY KEY,
    hotel_id    BIGINT NOT NULL,
    rating      NUMERIC(2) NOT NULL CHECK (rating BETWEEN 1 AND 10),
    FOREIGN KEY (hotel_id) REFERENCES hotel (id)
);
grant select, insert, update, delete on hotel_rating to postgres;