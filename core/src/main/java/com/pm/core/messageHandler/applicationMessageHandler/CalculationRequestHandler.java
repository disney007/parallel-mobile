package com.pm.core.messageHandler.applicationMessageHandler;

import com.pm.core.entity.CalculationJob;
import com.pm.core.entity.ConsumerDevice;
import com.pm.core.model.calculation.CalculationJobRequest;
import com.pm.core.model.message.ApplicationMessageType;
import com.pm.core.model.message.applicationMessage.ApplicationMessage;
import com.pm.core.model.message.applicationMessage.CalculationRequest;
import com.pm.core.repository.ConsumerDeviceRepository;
import com.pm.core.service.CalculationService;
import com.pm.core.service.NetworkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalculationRequestHandler implements ApplicationMessageHandler {
    final ConsumerDeviceRepository consumerDeviceRepository;
    final CalculationService calculationService;
    final NetworkService networkService;

    @Override
    public ApplicationMessageType getType() {
        return ApplicationMessageType.CAL_REQ;
    }

    @Override
    public void handle(ApplicationMessage message, String senderDeviceId) {
        CalculationRequest request = message.toData(CalculationRequest.class);
        log.info("request calculation {}", request);

        Optional<ConsumerDevice> consumerDevice = consumerDeviceRepository.findById(senderDeviceId);
        consumerDevice.ifPresent(device -> {
            CalculationJobRequest jobRequest = new CalculationJobRequest(request.getId(), request.getScript(), device.getOwner());
            CalculationJob calculationJob = calculationService.requestCalculationJob(jobRequest);
            log.info("calculation job created [{}]", calculationJob);

            calculationService.requestAvailableAgentDevice()
                    .ifPresent(agentDevice -> {
                        log.info("found agent device [{}]", agentDevice);
                        CalculationRequest forwardReq = new CalculationRequest(calculationJob.getId().toString(), request.getScript());
                        networkService.sendApplicationMessage(ApplicationMessageType.CAL_REQ, forwardReq, agentDevice.getDeviceId());
                    });
        });
    }
}
