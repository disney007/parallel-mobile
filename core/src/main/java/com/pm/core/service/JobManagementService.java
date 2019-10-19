package com.pm.core.service;

import com.pm.core.entity.AgentDevice;
import com.pm.core.entity.CalculationJob;
import com.pm.core.entity.CalculationJobExecution;
import com.pm.core.event.AgentDeviceAvailableEvent;
import com.pm.core.event.JobAvailableEvent;
import com.pm.core.model.calculation.CalJobExecState;
import com.pm.core.model.calculation.CalJobRequest;
import com.pm.core.model.calculation.CalJobResult;
import com.pm.core.model.calculation.CalJobState;
import com.pm.core.model.device.DeviceState;
import com.pm.core.model.message.ApplicationMessageType;
import com.pm.core.model.message.applicationMessage.CalculationRequest;
import com.pm.core.model.message.applicationMessage.CalculationResult;
import com.pm.core.repository.AgentDeviceRepository;
import com.pm.core.repository.CalculationJobRepository;
import com.pm.core.repository.ConsumerDeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobManagementService {

    final CalculationJobRepository calculationJobRepository;
    final AgentDeviceRepository agentDeviceRepository;
    final ConsumerDeviceRepository consumerDeviceRepository;
    final NetworkService networkService;
    final ApplicationEventPublisher applicationEventPublisher;
    final TransactionUtil transactionUtil;


    public void processJobResult(CalJobResult result) {
        log.info("process job result, execution id = {}", result.getId());
        transactionUtil.withTransaction(() -> {
            Optional<CalculationJob> calculationJob = calculationJobRepository.findByExecutionId(result.getId());
            if (!calculationJob.isPresent()) {
                log.warn("job not found for execution id [{}]", result.getId());
                return calculationJob;
            }

            calculationJob.ifPresent(job -> {
                job.getExecution(result.getId()).ifPresent(exec -> {
                    exec.setState(result.getState());
                    exec.setUpdateTimestamp(System.currentTimeMillis());
                    agentDeviceRepository.updateDeviceStatus(exec.getExecDeviceId(), DeviceState.IDLE);
                });

                job.setState(CalJobState.COMPLETED);
                job.setResult(result.getResult());
                job.setResultTimestamp(System.currentTimeMillis());
            });

            return calculationJob;
        }).ifPresent(job -> {
            consumerDeviceRepository.findFirstByOwner(job.getOwner()).ifPresent(consumerDevice -> {
                CalculationResult calculationResult = new CalculationResult(job.getRequestId(), job.getResult(), result.getState());
                networkService.sendApplicationMessage(ApplicationMessageType.CAL_RES, calculationResult, consumerDevice.getDeviceId());
                log.info("send result [jobId = {}] to [{}]", job.getId(), consumerDevice.getDeviceId());
            });
        });
        applicationEventPublisher.publishEvent(new AgentDeviceAvailableEvent());
    }


    public CalculationJob createJob(CalJobRequest request) {
        CalculationJob job = CalculationJob.builder()
                .id(UUID.randomUUID())
                .requestId(request.getRequestId())
                .script(request.getScript())
                .owner(request.getOwner())
                .state(CalJobState.READY)
                .createdTimestamp(System.currentTimeMillis())
                .build();
        calculationJobRepository.save(job);

        return job;
    }


    public void requestJob(CalJobRequest jobRequest) {
        CalculationJob job = transactionUtil.withTransaction(() -> createJob(jobRequest));
        log.info("new calculation job created [{}]", job.getInfo());
        applicationEventPublisher.publishEvent(new JobAvailableEvent());
    }


    @EventListener({JobAvailableEvent.class, AgentDeviceAvailableEvent.class})
    @Transactional
    public void processJob() {
        calculationJobRepository.findFirstByStateForUpdate(CalJobState.READY).ifPresent(job -> {
            log.info("start executing job [{}]", job.getInfo());
            agentDeviceRepository.findFirstByStateForUpdate(DeviceState.IDLE).ifPresent(device -> {
                log.info("found agent device [{}]", device);
                device.setState(DeviceState.RUNNING);

                CalculationJobExecution execution = new CalculationJobExecution(UUID.randomUUID(), job, device.getDeviceId(),
                        device.getOwner(), CalJobExecState.RUNNING, System.currentTimeMillis(), null);
                job.getExecutions().add(execution);
                job.setState(CalJobState.RUNNING);

                CalculationRequest forwardReq = new CalculationRequest(execution.getId().toString(), job.getScript());
                networkService.sendApplicationMessage(ApplicationMessageType.CAL_REQ, forwardReq, device.getDeviceId());
                log.info("send execution job [{}] to [{}]", execution.getId(), device.getDeviceId());
            });
        });
    }
}
