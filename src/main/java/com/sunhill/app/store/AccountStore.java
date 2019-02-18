package com.sunhill.app.store;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.sunhill.app.model.Account;

@Component
public class AccountStore {

	private ConcurrentHashMap<Integer, ArrayList<Account>> accounts = new ConcurrentHashMap<Integer, ArrayList<Account>>();

	public ConcurrentHashMap<Integer, ArrayList<Account>> getAccounts() {
		return accounts;
	}

	public void setAccounts(ConcurrentHashMap<Integer, ArrayList<Account>> accounts) {
		this.accounts = accounts;
	}


}
