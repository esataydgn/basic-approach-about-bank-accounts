package com.sunhill.app.service.converter;

import com.sunhill.app.model.User;
import com.sunhill.app.model.request.CreateUserRequest;
import com.sunhill.app.util.IdGenerator;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserConverter implements Function<CreateUserRequest, User> {

    @Override
    public User apply(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setId(IdGenerator.generate());
        user.setName(createUserRequest.getName());
        return user;
    }
}
