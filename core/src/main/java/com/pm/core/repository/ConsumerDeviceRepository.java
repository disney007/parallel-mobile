package com.pm.core.repository;

import com.pm.core.entity.ConsumerDevice;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ConsumerDeviceRepository extends PagingAndSortingRepository<ConsumerDevice, String> {
    Optional<ConsumerDevice> findFirstByOwner(String owner);
}
