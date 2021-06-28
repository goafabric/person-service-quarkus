create table person
(
	id varchar(36) not null
		constraint pk_person
			primary key,
    tenant_id varchar(3),
	first_name varchar(255),
	last_name varchar(255)
);

create table person_audit
(
	id varchar(36) not null
		constraint pk_person_audit
			primary key,
    tenant_id varchar(3),
	first_name varchar(255),
	last_name varchar(255),

    reference_id varchar(255),
    operation varchar(255),
    created_by varchar(255),
    created_at date,
    modified_by varchar(255),
    modified_at date,
    oldvalue TEXT,
    newvalue TEXT
);

