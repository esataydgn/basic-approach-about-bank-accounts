package com.sunhill.app.controller;

import com.sunhill.app.model.Account;
import com.sunhill.app.service.AccountService;
import com.sunhill.app.service.impl.AccountRetrieverService;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@RestController
public class AccountController {

    private final ApplicationContext applicationContext;
    private final AccountRetrieverService accountRetrieverService;

    public AccountController(ApplicationContext applicationContext, AccountRetrieverService accountRetrieverService) {
        this.applicationContext = applicationContext;
        this.accountRetrieverService = accountRetrieverService;
    }

    @PostMapping("/accounts/{accountId}/withdrawal/{amount}")
    public ResponseEntity<Account> withdrawal(@PathVariable String accountId, @PathVariable BigDecimal amount) {
        Account account = accountRetrieverService.getAccount(accountId);
        AccountService accountService = (AccountService) applicationContext.getBean(account.getAccountType().getAccountServiceBeanName());
        accountService.withdrawal(account, amount);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/accounts/{accountId}/deposit/{amount}")
    public ResponseEntity<Account> deposit(@PathVariable String accountId, @PathVariable BigDecimal amount) {
        Account account = accountRetrieverService.getAccount(accountId);
        AccountService accountService = (AccountService) applicationContext.getBean(account.getAccountType().getAccountServiceBeanName());
        accountService.deposit(account, amount);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/accounts/{accountId}/calculate")
    public ResponseEntity<Account> calculate(@PathVariable String accountId) {
        Account account = accountRetrieverService.getAccount(accountId);
        AccountService accountService = (AccountService) applicationContext.getBean(account.getAccountType().getAccountServiceBeanName());
        accountService.calculateRate(account);
        return ResponseEntity.ok(account);

    }

    @PostMapping("/source-accounts/{sourceAccountId}/destination-accounts/{destinationAccountId}/transfer/{amount}")
    public ResponseEntity<ArrayList> transfer(@PathVariable String sourceAccountId, @PathVariable String destinationAccountId, @PathVariable BigDecimal amount) {
        Account sourceAccount = accountRetrieverService.getAccount(sourceAccountId);
        Account destinationAccount = accountRetrieverService.getAccount(destinationAccountId);
        AccountService accountService = (AccountService) applicationContext.getBean(sourceAccount.getAccountType().getAccountServiceBeanName());
        ArrayList accounts = accountService.transfer(sourceAccount, destinationAccount, amount);
        return ResponseEntity.ok(accounts);
    }
}
