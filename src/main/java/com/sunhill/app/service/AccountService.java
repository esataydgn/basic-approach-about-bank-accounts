package com.sunhill.app.service;

import com.sunhill.app.model.Account;
import com.sunhill.app.model.enumtype.AccountType;

import java.math.BigDecimal;
import java.util.ArrayList;


public interface AccountService {

    Account withdrawal(Account account, BigDecimal amount);

    ArrayList transfer(Account sourceAccount, Account destinationAccount, BigDecimal amount);

    Account calculateRate(Account account);

    Account createAccount(AccountType accountType, Integer userId);

    Account deposit(Account account, BigDecimal amount);
}
