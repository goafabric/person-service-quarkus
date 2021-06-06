create table person
(
	id varchar(36) not null
		constraint pk_person
			primary key,
    tenant_id varchar(3),
	first_name varchar(255),
	last_name varchar(255)
);


