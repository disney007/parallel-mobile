package com.pm.core.messageHandler.applicationMessageHandler;

import com.pm.core.model.calculation.CalJobResult;
import com.pm.core.model.message.ApplicationMessageType;
import com.pm.core.model.message.applicationMessage.ApplicationMessage;
import com.pm.core.service.CalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalculationResultHandler implements ApplicationMessageHandler {
    final CalculationService calculationService;

    @Override
    public ApplicationMessageType getType() {
        return ApplicationMessageType.CAL_RES;
    }

    @Override
    public void handle(ApplicationMessage message, String senderDeviceId) {
        CalJobResult result = message.toData(CalJobResult.class);
        log.info("calculation result, id = [{}]", result.getId());
        calculationService.completeJob(result);
    }
}
