# !/bin/usr/python
print("Running ", __file__)

import os
import random
from typing import Generator
import numpy as np
import names
from pathlib import Path

ROOT = Path(os.getcwd())
QUANTITY = 150
MODEL = "recommendations"

print(ROOT)

target_path = ROOT/f"test_{MODEL}-v3.sql"
if os.path.exists(ROOT / f"{MODEL}-v3.sql"):
    print("Deleting existing SQL test script:", target_path)
    os.remove(target_path)

np.random.seed(777)
CREATE_SQL = """
CREATE TABLE recommendations
(
    recommendation_id BIGINT   AUTO_INCREMENT    NOT NULL,
    dest_id           BIGINT       NULL,
    author            VARCHAR(255) NULL,
    rate              INT          NULL,
    content           VARCHAR(255) NULL,
    CONSTRAINT pk_recommendations PRIMARY KEY (recommendation_id)
);
\n
"""

def generate_reviews():
    # -> Generator[str]:
    base_sql = """insert into `recommendations`(
        `dest_id`,
        `author`,
        `rate`,
        `content`

    ) VALUES (
                "{}","{}",{},"{}"
    );
    """
    content_str = "foobar"
    sql_list = []

    print("Writing SQL test script to:", target_path)
    with open(target_path, "w") as fh:
        for i in range(1,QUANTITY+1):
            if i == 1:
                fh.write(CREATE_SQL)
            # author == user_id: int
            a = random.randint(0, QUANTITY // 3)
            # content: str
            c = content_str * random.randint(1, (i % 11) + 2)
            # destination_id: int
            d = random.randint(0, QUANTITY+1)
            # review_subject: str
            r = random.randint(1, 100)

            sql_cmd = base_sql.format(
                d, a, r, c
            )
            fh.write(sql_cmd)
            sql_list.append(sql_cmd)

    def _generate():
        for _ in sql_list:
            yield _

    return _generate()


if __name__ == "__main__":
    g = generate_reviews()
    _g = next(g)
    print(_g)
