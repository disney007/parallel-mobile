create table public.account(
    username varchar primary key not null,
    name varchar not null,
    created_timestamp bigint not null,
    state varchar not null check(state in ('ACTIVE', 'DELETED'))
);

create table public.device_type(
    device_id varchar primary key not null,
    "type" varchar not null
);

create table public.agent_device(
    device_id varchar primary key not null,
    state varchar not null check (state in('IDLE', 'BUSY', 'RUNNING')),
    owner varchar not null,
    created_timestamp bigint not null
);
create index idx_agent_device_owner on public.agent_device(owner);

create table public.consumer_device(
    device_id varchar primary key not null,
    owner varchar not null,
    created_timestamp bigint not null
);
create index idx_consumer_device_owner on public.consumer_device(owner);


create table public.calculation_job(
    id UUID primary key not null,
    request_id varchar not null,
    script text not null,
    owner varchar not null,
    created_timestamp bigint not null,

    state varchar not null check (state in ('READY', 'RUNNING', 'COMPLETED', 'CANCELLED')),
    result text,
    result_timestamp bigint
);
create index idx_calculation_job_state on public.calculation_job(state);

create table public.calculation_job_execution(
    id UUID primary key not null,
    job_id UUID not null,
    exec_device_id varchar not null,
    exec_username varchar not null,
    state varchar not null check (state in ('RUNNING', 'TIMEOUT', 'ERROR', 'SUCCESS')),
    created_timestamp bigint not null,
    update_timestamp bigint
);
create index idx_calcualtion_job_execution_state on public.calculation_job_execution(state);
