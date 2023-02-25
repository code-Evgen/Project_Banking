delete from card;

insert into card (id, clientid, balance) values
(1, 1, 100);
insert into card (id, clientid, balance) values
(2, 1, 110);
insert into card (id, clientid, balance) values
(3, 2, 200);
insert into card (id, clientid, balance) values
(4, 2, 210);

-- alter table card alter column id set default nextval('id_seq');
alter sequence id_seq restart with 10;