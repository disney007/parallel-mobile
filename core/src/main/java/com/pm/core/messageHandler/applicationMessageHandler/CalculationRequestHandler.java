package com.pm.core.messageHandler.applicationMessageHandler;

import com.pm.core.entity.ConsumerDevice;
import com.pm.core.model.calculation.CalJobRequest;
import com.pm.core.model.message.ApplicationMessageType;
import com.pm.core.model.message.applicationMessage.ApplicationMessage;
import com.pm.core.model.message.applicationMessage.CalculationRequest;
import com.pm.core.repository.ConsumerDeviceRepository;
import com.pm.core.service.JobManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalculationRequestHandler implements ApplicationMessageHandler {
    final ConsumerDeviceRepository consumerDeviceRepository;
    final JobManagementService jobManagementService;


    @Override
    public ApplicationMessageType getType() {
        return ApplicationMessageType.CAL_REQ;
    }

    @Override
    public void handle(ApplicationMessage message, String senderDeviceId) {
        CalculationRequest request = message.toData(CalculationRequest.class);
        log.info("request calculation, id = [{}]", request.getId());

        Optional<ConsumerDevice> consumerDevice = consumerDeviceRepository.findById(senderDeviceId);
        consumerDevice.ifPresent(device -> {
            CalJobRequest jobRequest = new CalJobRequest(request.getId(), request.getScript(), device.getOwner());
            jobManagementService.requestJob(jobRequest);
        });
    }
}
