# USE internsample5;
# DROP TABLE user_table;
use micro;
drop table user_table;
CREATE TABLE user_table
(
    id    INT AUTO_INCREMENT NOT NULL,
    email      VARCHAR(255)       NULL,
    password   VARCHAR(255)       NULL,
    is_admin   BIT(1)             NULL,
    username   VARCHAR(255)       NULL,
    first_name VARCHAR(255)       NULL,
    last_name  VARCHAR(255)       NULL,
    dest_id    VARCHAR(255)       NULL,
    CONSTRAINT pk_user_table PRIMARY KEY (id)
);
# create table users
# (
#     user_id    INT          not null auto_increment,
#     email      varchar(255) not null,
#     is_admin   boolean      null,
#     username   varchar(255) null,
#     first_name varchar(255) null,
#     last_name  varchar(255) null,
#     password   varchar(255) null,
#     PRIMARY KEY (user_id) # column_7 int null
# );

# drop table users_table;
