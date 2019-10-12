package com.pm.core.repository;

import com.pm.core.entity.CalculationJob;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CalculationJobRepository extends PagingAndSortingRepository<CalculationJob, UUID> {

}
