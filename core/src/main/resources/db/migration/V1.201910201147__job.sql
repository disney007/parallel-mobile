alter table public.calculation_job
drop constraint calculation_job_state_check;
alter table public.calculation_job
add check (state in ('READY', 'RUNNING', 'COMPLETED', 'CANCELLED', 'RESULT_SENT_OUT'));
