package com.pm.core.repository;

import com.pm.core.entity.CalculationJob;
import com.pm.core.model.calculation.CalJobState;
import org.hibernate.LockMode;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CalculationJobRepository extends BaseRepository {


    public Optional<CalculationJob> findById(UUID jobId) {
        List<CalculationJob> result = session().createQuery("from CalculationJob where id = :id", CalculationJob.class)
                .setParameter("id", jobId)
                .list();

        return getFirst(result);
    }

    public Optional<CalculationJob> findByExecutionId(UUID jobExecutionId) {
        List<CalculationJob> list = session().createQuery("select exec.job from CalculationJobExecution as exec where exec.id=:jobExecutionId", CalculationJob.class)
                .setParameter("jobExecutionId", jobExecutionId)
                .list();
        return getFirst(list);
    }

    public Optional<CalculationJob> findFirstByStateForUpdate(CalJobState state) {
        List<CalculationJob> list = session()
                .createQuery("from CalculationJob as j where j.state = :state", CalculationJob.class)
                .setParameter("state", state)
                .setMaxResults(1)
                .setLockMode("j", LockMode.UPGRADE_SKIPLOCKED)
                .list();
        return getFirst(list);
    }

    public void save(CalculationJob job) {
        session().save(job);
    }

}
