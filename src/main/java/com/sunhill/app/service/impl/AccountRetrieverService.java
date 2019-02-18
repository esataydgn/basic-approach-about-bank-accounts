package com.sunhill.app.service.impl;

import com.sunhill.app.model.Account;
import com.sunhill.app.repository.AccountRepository;
import com.sunhill.app.service.validation.AccountValidationService;
import org.springframework.stereotype.Service;

@Service
public class AccountRetrieverService {

    private final AccountValidationService accountValidationService;
    private final AccountRepository accountRepository;

    protected AccountRetrieverService(AccountValidationService accountValidationService, AccountRepository accountRepository) {
        this.accountValidationService = accountValidationService;
        this.accountRepository = accountRepository;
    }

    public Account getAccount(String accountId) {
        Account account = accountRepository.findOne(accountId);
        accountValidationService.validateAccountIsPresent(account);
        return account;
    }
}
