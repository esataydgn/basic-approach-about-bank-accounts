package com.sunhill.app.service.impl;

import com.sunhill.app.exception.business.InterestedRateNotSupportedException;
import com.sunhill.app.model.Account;
import com.sunhill.app.model.enumtype.AccountType;
import com.sunhill.app.repository.AccountRepository;
import com.sunhill.app.service.AccountService;
import com.sunhill.app.service.validation.AccountValidationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CheckingAccountService implements AccountService {

    private static final BigDecimal FIX_ACCOUNT_BALANCE = new BigDecimal(5000);
    private static final BigDecimal FIX_WITHDRAWAL_LIMIT = new BigDecimal(1000);

    private final AccountRepository accountRepository;
    private final AccountValidationService accountValidationService;

    public CheckingAccountService(AccountRepository accountRepository, AccountValidationService accountValidationService) {
        this.accountRepository = accountRepository;
        this.accountValidationService = accountValidationService;
    }

    @Override
    public Account withdrawal(Account account, BigDecimal amount) {
        accountValidationService.validateWithdrawalLimit(account, amount);
        accountValidationService.validateSufficientBalance(account.getBalance(), amount);
        BigDecimal remainingBalance = account.getBalance().subtract(amount);
        account.setBalance(remainingBalance);
        return account;
    }

    @Override
    public ArrayList transfer(Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        accountValidationService.validateWithdrawalLimit(sourceAccount, amount);
        accountValidationService.validateSufficientBalance(sourceAccount.getBalance(), amount);
        BigDecimal remainingBalance = sourceAccount.getBalance().subtract(amount);
        sourceAccount.setBalance(remainingBalance);

        BigDecimal updatedBalance = destinationAccount.getBalance().add(amount);
        destinationAccount.setBalance(updatedBalance);
        return Stream.of(sourceAccount, destinationAccount).collect(Collectors.toCollection(ArrayList::new));

    }

    @Override
    public Account calculateRate(Account account) {
        throw new InterestedRateNotSupportedException("Interest rate does not support by checking accounts.");

    }

    @Override
    public Account createAccount(AccountType accountType, Integer userId) {
        Account newAccount = new Account();
        newAccount.setAccountId(UUID.randomUUID().toString());
        newAccount.setAccountType(accountType);
        newAccount.setWithdrawalLimit(FIX_WITHDRAWAL_LIMIT);
        newAccount.setBalance(FIX_ACCOUNT_BALANCE);
        newAccount.setUserId(userId);
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
