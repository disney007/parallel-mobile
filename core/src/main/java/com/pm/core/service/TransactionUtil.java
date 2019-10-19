package com.pm.core.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
public class TransactionUtil {
    @Transactional
    public <T> T withTransaction(Supplier<T> supplier) {
        return supplier.get();
    }

    @Transactional
    public void withTransaction(Runnable runnable) {
        runnable.run();
    }
}
