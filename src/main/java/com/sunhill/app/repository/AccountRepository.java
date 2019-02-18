package com.sunhill.app.repository;

import com.sunhill.app.model.Account;
import com.sunhill.app.store.Store;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository {

    private final Store store;

    public AccountRepository(Store store) {
        this.store = store;
    }

    public Account findOne(String accountId) {
        return store.getAccounts().get(accountId);
    }

    public Account save(Account account) {
        return store.getAccounts().put(account.getAccountId(),account);
    }
}
