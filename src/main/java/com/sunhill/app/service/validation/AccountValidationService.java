package com.sunhill.app.service.validation;

import com.sunhill.app.exception.business.AccountNotFoundException;
import com.sunhill.app.exception.business.InsufficientBalanceException;
import com.sunhill.app.exception.business.WithdrawalLimitException;
import com.sunhill.app.model.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountValidationService {

    public void validateAccountIsPresent(Account account) {
        if (account == null) {
            throw new AccountNotFoundException("Account not found");
        }
    }

    public void validateSufficientBalance(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("You do not have enough balance...");
        }
    }

    public void validateWithdrawalLimit(Account account, BigDecimal amount) {
        if (account.getWithdrawalLimit().compareTo(amount) < 0) {
            throw new WithdrawalLimitException("This account withdrawal limit is " + account.getWithdrawalLimit() + " per request");
        }
    }
}
