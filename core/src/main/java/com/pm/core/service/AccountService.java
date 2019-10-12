package com.pm.core.service;

import com.pm.core.entity.Account;
import com.pm.core.model.State;
import com.pm.core.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    final AccountRepository accountRepository;

    public Account generateRandomAccount() {
        Account account = new Account(UUID.randomUUID().toString(), "random", System.currentTimeMillis(), State.ACTIVE);
        accountRepository.save(account);
        return account;
    }
}
