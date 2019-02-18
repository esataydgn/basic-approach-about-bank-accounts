package com.sunhill.app.service.validation;

import com.sunhill.app.exception.validation.InvalidUserNameException;
import com.sunhill.app.model.request.CreateUserRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CreateUserValidationService {

    public void validate(CreateUserRequest createUserRequest) {
        if (StringUtils.isEmpty(createUserRequest.getName())) {
            throw new InvalidUserNameException("User Name Cannot Be Empty");
        }
    }
}
