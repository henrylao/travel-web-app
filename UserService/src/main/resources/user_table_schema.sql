CREATE TABLE user_table
(
    id         INT          NOT NULL auto_increment,
    email      VARCHAR(255) NULL,
    password   VARCHAR(255) NULL,
    is_admin   BIT(1)       NULL,
    username   VARCHAR(255) NULL,
    first_name VARCHAR(255) NULL,
    last_name  VARCHAR(255) NULL,
    CONSTRAINT pk_user_table PRIMARY KEY (id)
);