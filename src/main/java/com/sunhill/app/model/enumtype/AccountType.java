package com.sunhill.app.model.enumtype;

public enum AccountType {

    SAVING("savingAccountService"), CHECKING("checkingAccountService");

    private final String accountServiceBeanName;

    AccountType(String accountServiceBeanName) {
        this.accountServiceBeanName = accountServiceBeanName;
    }

    public String getAccountServiceBeanName() {
        return accountServiceBeanName;
    }
}
