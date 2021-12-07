CREATE TABLE destinations
(
    id        INT AUTO_INCREMENT NOT NULL,
    place     VARCHAR(255)       NULL,
    country   VARCHAR(255)       NULL,
    latitude  FLOAT              NULL,
    longitude FLOAT              NULL,
    info      VARCHAR(255)       NULL,
    image     VARCHAR(255)       NULL,
    CONSTRAINT pk_destination PRIMARY KEY (id)
);