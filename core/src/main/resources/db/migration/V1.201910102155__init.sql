create table public.device_type(
    device_id varchar primary key not null,
    "type" varchar not null
);
create table public.agent_device(
    device_id varchar primary key not null,
    state varchar not null check (state in('IDLE', 'BUSY', 'RUNNING')),
    created_timestamp bigint not null
);

create table public.consumer_device(
    device_id varchar primary key not null,
    created_timestamp bigint not null
);
