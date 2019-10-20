package com.pm.core.messageHandler.applicationMessageHandler;

import com.pm.core.event.JobCompleteEvent;
import com.pm.core.model.calculation.CalJobResult;
import com.pm.core.model.message.ApplicationMessageType;
import com.pm.core.model.message.applicationMessage.ApplicationMessage;
import com.pm.core.service.JobManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalculationResultHandler implements ApplicationMessageHandler {
    final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public ApplicationMessageType getType() {
        return ApplicationMessageType.CAL_RES;
    }

    @Override
    public void handle(ApplicationMessage message, String senderDeviceId) {
        CalJobResult result = message.toData(CalJobResult.class);
        applicationEventPublisher.publishEvent(new JobCompleteEvent(result));
    }
}
