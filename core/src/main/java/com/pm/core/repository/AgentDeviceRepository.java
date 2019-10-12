package com.pm.core.repository;

import com.pm.core.entity.AgentDevice;
import com.pm.core.model.device.DeviceState;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentDeviceRepository extends PagingAndSortingRepository<AgentDevice, String> {

    Optional<AgentDevice> findFirstByState(DeviceState state);
}
