package com.pm.core.messageHandler;

import com.pm.core.event.ResultSentToConsumerEvent;
import com.pm.core.model.Keywords;
import com.pm.core.model.message.Message;
import com.pm.core.model.message.MessageConfirmation;
import com.pm.core.model.message.MessageType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageConfirmationMessageHandler implements MessageHandler {
    final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public MessageType getType() {
        return MessageType.MESSAGE_CONFIRMATION;
    }

    @Override
    public void handle(Message message) {
        MessageConfirmation messageConfirmation = message.toData(MessageConfirmation.class);
        String reference = messageConfirmation.getReference();
        if (StringUtils.startsWithIgnoreCase(reference, Keywords.REFERENCE_JOB_ID)) {
            String jobId = StringUtils.removeStartIgnoreCase(reference, Keywords.REFERENCE_JOB_ID);
            applicationEventPublisher.publishEvent(new ResultSentToConsumerEvent(UUID.fromString(jobId)));
        }
    }
}
