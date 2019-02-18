package com.sunhill.app.service.impl;

import com.sunhill.app.exception.business.TransferNotSupportedException;
import com.sunhill.app.model.Account;
import com.sunhill.app.model.enumtype.AccountType;
import com.sunhill.app.repository.AccountRepository;
import com.sunhill.app.service.AccountService;
import com.sunhill.app.service.validation.AccountValidationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class SavingAccountService implements AccountService {

    private static final BigDecimal FIX_ACCOUNT_BALANCE = new BigDecimal(5000);
    private static final BigDecimal FIX_ACCOUNT_INTEREST_RATE = new BigDecimal(0.5);

    private final AccountValidationService accountValidationService;
    private final AccountRepository accountRepository;

    public SavingAccountService(AccountValidationService accountValidationService,
                                AccountRepository accountRepository) {
        this.accountValidationService = accountValidationService;
        this.accountRepository = accountRepository;
    }

    @Override
    public Account withdrawal(Account account, BigDecimal amount) {
        accountValidationService.validateSufficientBalance(account.getBalance(), amount);
        BigDecimal remainingBalance = account.getBalance().subtract(amount);
        account.setBalance(remainingBalance);
        return account;
    }

    @Override
    public ArrayList transfer(Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        throw new TransferNotSupportedException("Saving accounts cannot support transfer between accounts...");
    }

    @Override
    public Account calculateRate(Account account) {
        BigDecimal updatedBalance = account.getBalance().multiply(account.getInterestRate().add(BigDecimal.ONE));
        account.setBalance(updatedBalance);
        return account;
    }

    @Override
    public Account createAccount(AccountType accountType, Integer userId) {
        Account newAccount = new Account();
        newAccount.setAccountId(UUID.randomUUID().toString());
        newAccount.setAccountType(accountType);
        newAccount.setBalance(FIX_ACCOUNT_BALANCE);
        newAccount.setUserId(userId);
        newAccount.setInterestRate(FIX_ACCOUNT_INTEREST_RATE);
        accountRepository.save(newAccount);

        return newAccount;
    }

    @Override
    public Account deposit(Account account, BigDecimal amount) {
        BigDecimal updatedBalance = account.getBalance().add(amount);
        account.setBalance(updatedBalance);
        return account;
    }
}
