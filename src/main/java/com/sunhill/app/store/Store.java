package com.sunhill.app.store;


import com.sunhill.app.model.Account;
import com.sunhill.app.model.User;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class Store {

    private ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<Integer, User>();

    public ConcurrentHashMap<String, Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ConcurrentHashMap<String, Account> accounts) {
        this.accounts = accounts;
    }

    private ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer, User> getUsers() {
        return users;
    }

    public void setUsers(ConcurrentHashMap<Integer, User> users) {
        this.users = users;
    }


}
