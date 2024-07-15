CREATE TABLE public."product"
(
    "id"       SERIAL PRIMARY KEY,
    "name"     TEXT    NOT NULL,
    "quantity" INTEGER NOT NULL,
    "price"    INTEGER NOT NULL,
    "deleted"  TIMESTAMP
    -- index does not work because in postgres: null != null
    -- UNIQUE (name, deleted)
);
INSERT INTO public.product ("name", quantity, price, deleted) VALUES('Knuspr 1', 100, 1, null);
INSERT INTO public.product ("name", quantity, price, deleted) VALUES('Knuspr 2', 100, 1, null);
INSERT INTO public.product ("name", quantity, price, deleted) VALUES('Knuspr 3', 100, 1, null);


CREATE TABLE public."order"
(
    "id"      SERIAL PRIMARY KEY,
    "name"    TEXT    NOT NULL,
    "paid"    BOOLEAN NOT NULL DEFAULT false,
    "created" TIMESTAMP        DEFAULT NOW()
);
INSERT INTO public."order" ("name", paid, created) VALUES('Order 1', false, now());
INSERT INTO public."order" ("name", paid, created) VALUES('Order 2', false, now());


CREATE TABLE public."product_order"
(
    id_product_id INTEGER NOT NULL,
    id_order_id   INTEGER NOT NULL,
    PRIMARY KEY (id_product_id, id_order_id),
    FOREIGN KEY (id_product_id) REFERENCES "product" (id),
    FOREIGN KEY (id_order_id) REFERENCES "order" (id),
    "quantity" INTEGER NOT NULL
);
INSERT INTO public.product_order (id_product_id, id_order_id, quantity) VALUES(1, 1, 1);
INSERT INTO public.product_order (id_product_id, id_order_id, quantity) VALUES(2, 1, 2);
INSERT INTO public.product_order (id_product_id, id_order_id, quantity) VALUES(3, 1, 3);

INSERT INTO public.product_order (id_product_id, id_order_id, quantity) VALUES(1, 2, 5);

