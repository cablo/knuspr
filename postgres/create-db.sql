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
    id_product_id INTEGER NOT NULL,
    id_order_id   INTEGER NOT NULL,
    PRIMARY KEY (id_product_id, id_order_id),
    FOREIGN KEY (id_product_id) REFERENCES "product" (id),
    FOREIGN KEY (id_order_id) REFERENCES "order" (id),
    "quantity" INTEGER NOT NULL
);

