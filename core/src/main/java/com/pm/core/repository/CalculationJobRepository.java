package com.pm.core.repository;

import com.pm.core.entity.CalculationJob;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CalculationJobRepository extends PagingAndSortingRepository<CalculationJob, UUID> {

    @Query("select exec.job from CalculationJobExecution as exec where exec.id=:jobExecutionId")
    Optional<CalculationJob> findCalculationJob(@Param("jobExecutionId") UUID jobExecutionId);
}
