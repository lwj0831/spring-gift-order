INSERT INTO product (id, name, price, description, image_url)
VALUES (100, '테스트 상품0', 100, '설명1', 'https://img.url');
INSERT INTO product (id, name, price, description, image_url)
VALUES (101, '테스트 상품1', 1000, '설명2', 'https://img.url');
INSERT INTO product (id, name, price, description, image_url)
VALUES (102, '테스트 상품2', 2000, '설명2', 'https://img.url');
INSERT INTO product (id, name, price, description, image_url)
VALUES (103, '테스트 상품3', 3000, '설명3', 'https://img.url');
INSERT INTO product (id, name, price, description, image_url)
VALUES (104, '테스트 상품4', 4000, '설명4', 'https://img.url');
INSERT INTO product (id, name, price, description, image_url)
VALUES (105, '테스트 상품5', 5000, '설명5', 'https://img.url');
INSERT INTO product (id, name, price, description, image_url)
VALUES (106, '테스트 상품6', 6000, '설명6', 'https://img.url');
INSERT INTO product (id, name, price, description, image_url)
VALUES (107, '테스트 상품7', 7000, '설명7', 'https://img.url');
INSERT INTO product (id, name, price, description, image_url)
VALUES (108, '테스트 상품8', 8000, '설명8', 'https://img.url');
INSERT INTO product (id, name, price, description, image_url)
VALUES (109, '테스트 상품9', 9000, '설명9', 'https://img.url');
INSERT INTO product (id, name, price, description, image_url)
VALUES (110, '테스트 상품10', 10000, '설명10', 'https://img.url');


INSERT INTO member (id, name)
VALUES (100, 'lee');


INSERT INTO member_auth (id, email, password, refresh_token)
VALUES (100, 'wjl0831@gmail.com', '$2a$10$JunqFWsxQbDLzmNAAgXNKuKW77ehhv9cuozq75SkkbJZEYtee1Zm.',
        'abcd');


INSERT INTO wish_item (member_id, product_id)
VALUES (100, 100);
INSERT INTO wish_item (member_id, product_id)
VALUES (100, 101);
INSERT INTO wish_item (member_id, product_id)
VALUES (100, 103);
INSERT INTO wish_item (member_id, product_id)
VALUES (100, 105);
INSERT INTO wish_item (member_id, product_id)
VALUES (100, 107);
INSERT INTO wish_item (member_id, product_id)
VALUES (100, 109);
INSERT INTO wish_item (member_id, product_id)
VALUES (100, 110);

INSERT INTO product_option (id, name, quantity, product_id)
VALUES (100, '옵션명1', 100, 100);
INSERT INTO product_option (id, name, quantity, product_id)
VALUES (101, '옵션명2', 200, 100);
INSERT INTO product_option (id, name, quantity, product_id)
VALUES (102, '옵션명3', 300, 100);
INSERT INTO product_option (id, name, quantity, product_id)
VALUES (103, '옵션명4', 400, 100);
INSERT INTO product_option (id, name, quantity, product_id)
VALUES (104, '옵션명5', 500, 100);



