package com.sunhill.app.service.impl;

import com.sunhill.app.model.User;
import com.sunhill.app.model.enumtype.AccountType;
import com.sunhill.app.model.request.CreateUserRequest;
import com.sunhill.app.repository.UserRepository;
import com.sunhill.app.service.converter.UserConverter;
import com.sunhill.app.service.validation.CreateUserValidationService;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final CreateUserValidationService createUserValidationService;
    private final UserConverter userConverter;
    private final UserRepository userRepository;
    private final CheckingAccountService checkingAccountService;
    private final SavingAccountService savingAccountService;

    public UserService(CreateUserValidationService createUserValidationService, UserConverter userConverter, UserRepository userRepository, CheckingAccountService checkingAccountService, SavingAccountService savingAccountService) {
        this.createUserValidationService = createUserValidationService;
        this.userConverter = userConverter;
        this.userRepository = userRepository;
        this.checkingAccountService = checkingAccountService;
        this.savingAccountService = savingAccountService;
    }

    public User createUser(CreateUserRequest createUserRequest) {
        createUserValidationService.validate(createUserRequest);
        User user = userConverter.apply(createUserRequest);
        userRepository.save(user);
        user.getAccounts().add(savingAccountService.createAccount(AccountType.SAVING, user.getId()));
        user.getAccounts().add(checkingAccountService.createAccount(AccountType.CHECKING, user.getId()));
        return user;

    }
}
