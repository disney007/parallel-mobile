package com.pm.core.repository;

import com.pm.core.entity.DeviceTypeInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceTypeInfoRepository extends CrudRepository<DeviceTypeInfo, String> {
}
