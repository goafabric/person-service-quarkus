INSERT INTO address (id, tenant_id, street, city, version)
VALUES ('5cae4fc5-1fc2-46aa-a6e4-55267d17256e','0','Evergreen Terrace 1','Springfield 0','0');

INSERT INTO address (id, tenant_id, street, city, version)
VALUES ('64504ada-0bfd-495e-9d92-731414271a7f','0','Everblue Terrace 1','Springfield 0','0');

INSERT INTO address (id, tenant_id, street, city, version)
VALUES ('88e13294-877a-49e3-8c72-ab5e34004ca4','0','Monty Mansion','Springfield 0','0');

INSERT INTO person (id,tenant_id,first_name,last_name,address_id,version)
VALUES ('392ecbba-7b30-40d9-b46a-564f7d8fd5c4','0','Homer','Simpson','5cae4fc5-1fc2-46aa-a6e4-55267d17256e','0');

INSERT INTO person (id,tenant_id,first_name,last_name,address_id,version)
VALUES ('50d5b605-55e1-41b4-bf8f-53e586715c3e','0','Bart','Simpson','64504ada-0bfd-495e-9d92-731414271a7f','0');

INSERT INTO person (id,tenant_id,first_name,last_name,address_id,version)
VALUES ('5d103149-be15-4257-ab18-1d451d1e308a','0','Monty','Burns','88e13294-877a-49e3-8c72-ab5e34004ca4','0');

