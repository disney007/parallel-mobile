package com.pm.core.repository;

import com.pm.core.entity.Agent;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgentRepository extends PagingAndSortingRepository<Agent, UUID> {

}
