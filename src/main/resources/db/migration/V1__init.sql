create table person
(
	id varchar(36) not null
		constraint pk_person
			primary key,
    tenant_id varchar(36),
	first_name varchar(255),
	last_name varchar(255)
);

create index idx_person_tenant_id on person(tenant_id);

create table person_audit
(
	id varchar(36) not null
		constraint pk_person_audit
			primary key,
    tenant_id varchar(36),

    reference_id varchar(255),
    operation varchar(255),
    created_by varchar(255),
    created_at date,
    modified_by varchar(255),
    modified_at date,
    oldvalue TEXT,
    newvalue TEXT
);

create index idx_person_audit_tenant_id on person_audit(tenant_id);
