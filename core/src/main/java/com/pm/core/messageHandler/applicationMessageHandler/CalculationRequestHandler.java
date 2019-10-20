package com.pm.core.messageHandler.applicationMessageHandler;

import com.pm.core.event.JobRequestEvent;
import com.pm.core.model.calculation.CalJobRequest;
import com.pm.core.model.message.ApplicationMessageType;
import com.pm.core.model.message.applicationMessage.ApplicationMessage;
import com.pm.core.model.message.applicationMessage.CalculationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalculationRequestHandler implements ApplicationMessageHandler {
    final ApplicationEventPublisher applicationEventPublisher;


    @Override
    public ApplicationMessageType getType() {
        return ApplicationMessageType.CAL_REQ;
    }

    @Override
    public void handle(ApplicationMessage message, String senderDeviceId) {
        CalculationRequest request = message.toData(CalculationRequest.class);
        CalJobRequest jobRequest = new CalJobRequest(request.getId(), request.getScript(), senderDeviceId);
        applicationEventPublisher.publishEvent(new JobRequestEvent(jobRequest));
    }
}
