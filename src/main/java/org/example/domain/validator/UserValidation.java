package org.example.domain.validator;

import org.example.domain.User;

public class UserValidation implements Validator<User> {

    @Override
    public void validate(User entity) throws ValidationException {
        String errorMessage = "";

        if (entity.getUsername().isEmpty()) {
            errorMessage += "Username name can't be null! ";
        }
        if (entity.getRealName().isEmpty()) {
            errorMessage += "Real name can't be null! ";
        }
        System.out.println(errorMessage);
        if (!errorMessage.isEmpty()) {
            throw new ValidationException(errorMessage);
        }
    }
}
