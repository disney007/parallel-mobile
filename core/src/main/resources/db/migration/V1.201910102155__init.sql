create table public.agent(
    id UUID primary key not null,
    device_id varchar not null,
    state varchar not null check (state in('IDLE', 'BUSY', 'RUNNING')),
    created_timestamp bigint not null
);

create index idx_agent_device_id on public.agent(device_id);
