create table examples (
    id uniqueidentifier not null primary key,
    example_value varchar(255) not null,
    created_at datetimeoffset(7) not null
);
