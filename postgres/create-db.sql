CREATE TABLE public."product"
(
    "id"       SERIAL PRIMARY KEY,
    "name"     TEXT    NOT NULL,
    "quantity" INTEGER NOT NULL,
    "price"    INTEGER NOT NULL,
    "deleted"  TIMESTAMP
);
-- INSERT INTO public."product" ("name", quantity, price) VALUES('Knuspr', 100, 2);
-- INSERT INTO public."product" ("name", quantity, price) VALUES('Bread', 50, 40);

CREATE TABLE public."order"
(
    "id"      SERIAL PRIMARY KEY,
    "name"    TEXT    NOT NULL,
    "payed"   BOOLEAN NOT NULL DEFAULT false,
    "created" TIMESTAMP        DEFAULT NOW(),
    "deleted" TIMESTAMP
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

