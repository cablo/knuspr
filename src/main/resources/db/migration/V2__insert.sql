INSERT INTO public.product ("name", quantity, price, deleted) VALUES('Knuspr 1', 100, 1, null);
INSERT INTO public.product ("name", quantity, price, deleted) VALUES('Knuspr 2', 100, 1, null);
INSERT INTO public.product ("name", quantity, price, deleted) VALUES('Knuspr 3', 100, 1, null);

INSERT INTO public."order" ("name", paid, created) VALUES('Order 1', false, now());
INSERT INTO public."order" ("name", paid, created) VALUES('Order 2', false, now());

INSERT INTO public.product_order (id_product_id, id_order_id, quantity) VALUES(1, 1, 1);
INSERT INTO public.product_order (id_product_id, id_order_id, quantity) VALUES(2, 1, 2);
INSERT INTO public.product_order (id_product_id, id_order_id, quantity) VALUES(3, 1, 3);
INSERT INTO public.product_order (id_product_id, id_order_id, quantity) VALUES(1, 2, 5);

