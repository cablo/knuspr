CREATE TABLE public."product"
(
    "id"       SERIAL PRIMARY KEY,
    "name"     TEXT    NOT NULL,
    "quantity" INTEGER NOT NULL,
    "price"    INTEGER NOT NULL,
    "deleted"  TIMESTAMP,
    -- TODO index nefunguje
    UNIQUE (name, deleted)
);

CREATE TABLE public."order"
(
    "id"      SERIAL PRIMARY KEY,
    "name"    TEXT    NOT NULL,
    "paid"    BOOLEAN NOT NULL DEFAULT false,
    "created" TIMESTAMP        DEFAULT NOW()
);

CREATE TABLE public."product_order"
(
    product_id INTEGER NOT NULL,
    order_id   INTEGER NOT NULL,
    PRIMARY KEY (product_id, order_id),
    FOREIGN KEY (product_id) REFERENCES "product" (id),
    FOREIGN KEY (order_id) REFERENCES "order" (id),
    "quantity" INTEGER NOT NULL
);

