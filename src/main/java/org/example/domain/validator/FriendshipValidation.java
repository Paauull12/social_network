package org.example.domain.validator;

import org.example.domain.Friendship;

import java.util.Objects;

public class FriendshipValidation implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        String errorMessage = "";

        if(entity.getUser1() == entity.getUser2())
            errorMessage += "You cannot befriend the same user!";

        System.out.println(errorMessage);
        if (!errorMessage.isEmpty()) {
            throw new ValidationException(errorMessage);
        }
    }
}
