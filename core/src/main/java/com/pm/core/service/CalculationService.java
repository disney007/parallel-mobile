package com.pm.core.service;

import com.pm.core.entity.AgentDevice;
import com.pm.core.entity.CalculationJob;
import com.pm.core.model.calculation.CalculationJobRequest;
import com.pm.core.model.device.DeviceState;
import com.pm.core.repository.AgentDeviceRepository;
import com.pm.core.repository.CalculationJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalculationService {

    final CalculationJobRepository calculationJobRepository;
    final AgentDeviceRepository agentDeviceRepository;

    public CalculationJob requestCalculationJob(CalculationJobRequest request) {
        CalculationJob job = CalculationJob.builder()
                .id(UUID.randomUUID())
                .requestId(request.getRequestId())
                .script(request.getScript())
                .owner(request.getOwner())
                .createdTimestamp(System.currentTimeMillis())
                .build();
        calculationJobRepository.save(job);

        return job;
    }

    public Optional<AgentDevice> requestAvailableAgentDevice() {
        return agentDeviceRepository.findFirstByState(DeviceState.IDLE);
    }
}
