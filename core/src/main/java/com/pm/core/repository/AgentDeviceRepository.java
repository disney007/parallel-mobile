package com.pm.core.repository;

import com.pm.core.entity.AgentDevice;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentDeviceRepository extends PagingAndSortingRepository<AgentDevice, String> {

}
