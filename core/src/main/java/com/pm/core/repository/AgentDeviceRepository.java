package com.pm.core.repository;

import com.pm.core.entity.AgentDevice;
import com.pm.core.model.device.DeviceState;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class AgentDeviceRepository {
    @PersistenceContext
    EntityManager entityManager;

    public Optional<AgentDevice> findFirstByStateForUpdate(DeviceState state) {
        Session session = entityManager.unwrap(Session.class);
        List<AgentDevice> list = session.createQuery("from AgentDevice as d where d.state = :state", AgentDevice.class)
                .setParameter("state", state)
                .setMaxResults(1)
                .setLockMode("d", LockMode.UPGRADE_SKIPLOCKED)
                .list();
        return list.size() == 1 ? Optional.of(list.get(0)) : Optional.empty();
    }

    public void save(AgentDevice device) {
        Session session = entityManager.unwrap(Session.class);
        session.save(device);
    }

    public void deleteById(String id) {
        Session session = entityManager.unwrap(Session.class);
        session.createQuery("delete from AgentDevice as d where d.deviceId = :deviceId")
                .setParameter("deviceId", id)
                .executeUpdate();
    }

    Optional<AgentDevice> getById(String deviceId) {
        Session session = entityManager.unwrap(Session.class);
        return Optional.ofNullable(session.get(AgentDevice.class, deviceId));
    }

    public Optional<AgentDevice> updateDeviceStatus(String deviceId, DeviceState state) {
        return getById(deviceId).map(device -> {
            device.setState(state);
            return device;
        });
    }
}
