import numpy as np
import names

np.random.seed(777)
STR_FILL_1 = "ipsum lorem"
STR_FILL_2 = "foo bar"
QUANTITY = 100
CREATE_DB_SQL = """CREATE DATABASE IF NOT EXISTS micro;\n"""
CREATE_TABLE_SQL = """create table reviews
                (
                    id             int auto_increment
                        primary key,
                    author         varchar(255) null,
                    content        varchar(255) null,
                    destination_id int          null,
                    review_subject varchar(255) null
                );
"""
base_sql = """insert into `Users`(
    `username`,
    `password`,
    `is_admin`,
    `first_name`,
    `last_name`,
    `email`

) VALUES (
             '{}','{}',{},'{}','{}','{}'
);
"""
with open("test_users.sql", "w") as fh:
    for i in range(1, QUANTITY + 1):
        if i == 0:
            fh.write(CREATE_DB_SQL)
            fh.write(CREATE_TABLE_SQL)
        u = "testUser{}".format(i)
        a, b = np.random.randint(1, 50), np.random.randint(100, 150)
        p = "Hello@World{}{}".format(a, b)
        ia = False
        full = names.get_full_name()
        f, l = [_.strip() for _ in full.split()]
        e = f"{a}{f}{b}@123.com"
        fh.write(base_sql.format(u, p, ia, f, l, e))
