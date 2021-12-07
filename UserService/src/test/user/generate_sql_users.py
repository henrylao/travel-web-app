# !/bin/usr/python
import numpy as np
import names
np.random.seed(777)
QUANTITY = 100

print("Running ", __file__)
MODEL = "user"
CREATE_SQL = """
CREATE TABLE user_table
(
    user_id    INT          NOT NULL,
    email      VARCHAR(255) NULL,
    password   VARCHAR(255) NULL,
    is_admin   BIT(1)       NULL,
    username   VARCHAR(255) NULL,
    first_name VARCHAR(255) NULL,
    last_name  VARCHAR(255) NULL,
    CONSTRAINT pk_user_table PRIMARY KEY (user_id)
);
);\n
"""
base_sql = """insert into `user_table`(
    `username`,
    `password`,
    `is_admin`,
    `first_name`,
    `last_name`,
    `email`

) VALUES (
             "{}","{}",{},"{}","{}","{}"
);\n
"""
with open("test_users.sql", "w") as fh:
    for i in range(1, QUANTITY + 1):
        if i == 1:
           fh.write(CREATE_SQL)
        u = "testUser{}".format(i)
        a,b = np.random.randint(1,50), np.random.randint(100,150)
        p = "Hello@World{}{}".format(a,b)
        ia = False
        full = names.get_full_name()
        f,l = [ _.strip() for _ in full.split()]
        e = f"{a}{f}{b}@123.com"
        fh.write(base_sql.format(
            u, p, ia, f, l,e
        ))
        print(base_sql.format(
                          u, p, ia, f, l,e
                      ))
    


