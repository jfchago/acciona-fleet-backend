create table carfleet_requests (
    id uniqueidentifier not null constraint pk_carfleet_requests primary key,
    sdn varchar(100) not null, registration varchar(50) null, contract_start date null,
    state int null, cancellation_date date null, contract_term_months int null,
    contract_end_date date null, card_last_four_digits char(4) null,
    retired bit not null constraint df_carfleet_requests_retired default 0,
    version bigint not null constraint df_carfleet_requests_version default 0,
    updated_at datetimeoffset(7) not null constraint df_carfleet_requests_updated default sysdatetimeoffset()
);
create index ix_carfleet_requests_sdn on carfleet_requests(sdn);
create table carfleet_request_audit (
    audit_id bigint identity(1,1) not null constraint pk_carfleet_request_audit primary key,
    request_id uniqueidentifier not null, action varchar(30) not null, actor varchar(255) not null,
    changed_fields varchar(max) not null,
    created_at datetimeoffset(7) not null constraint df_carfleet_request_audit_created default sysdatetimeoffset(),
    constraint fk_carfleet_request_audit_request foreign key(request_id) references carfleet_requests(id)
);
create index ix_carfleet_request_audit_request on carfleet_request_audit(request_id, created_at);
