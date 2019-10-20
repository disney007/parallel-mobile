package com.pm.core.messageHandler;

import com.pm.core.model.device.DeviceType;
import com.pm.core.model.message.DeviceConnectionRecord;
import com.pm.core.model.message.Message;
import com.pm.core.model.message.MessageType;
import com.pm.core.repository.AgentDeviceRepository;
import com.pm.core.repository.ConsumerDeviceRepository;
import com.pm.core.repository.DeviceTypeInfoRepository;
import com.pm.core.service.TransactionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceDisconnectedMessageHandler implements MessageHandler {

    final AgentDeviceRepository agentDeviceRepository;
    final ConsumerDeviceRepository consumerDeviceRepository;
    final DeviceTypeInfoRepository deviceTypeInfoRepository;
    final ExecutorService executorService;
    final TransactionUtil transactionUtil;

    @Override
    public MessageType getType() {
        return MessageType.USER_DISCONNECTED;
    }

    @Override
    public void handle(Message message) {
        final DeviceConnectionRecord record = message.toData(DeviceConnectionRecord.class);
        executorService.execute(() -> this.process(record));
    }

    void process(DeviceConnectionRecord record) {
        final String deviceId = record.getDeviceId();
        log.info("device disconnected: [{}]", deviceId);
        transactionUtil.withTransaction(() -> {
            deviceTypeInfoRepository.findById(deviceId).ifPresent(deviceTypeInfo -> {
                if (DeviceType.AGENT.equals(deviceTypeInfo.getType())) {
                    agentDeviceRepository.deleteById(deviceId);
                } else if (DeviceType.CONSUMER.equals(deviceTypeInfo.getType())) {
                    consumerDeviceRepository.deleteById(deviceId);
                }
                deviceTypeInfoRepository.deleteById(deviceId);
            });
        });
    }
}
