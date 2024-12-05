package org.example.projfinal.domain.validator;


import org.example.projfinal.domain.Friendship;

public class FriendshipValidation implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        String errorMessage = "";

        if(entity.getUser1().equals(entity.getUser2()))
            errorMessage += "You cannot befriend the same user!";
        if(!errorMessage.isEmpty())
            System.out.println(errorMessage);
        if (!errorMessage.isEmpty()) {
            throw new ValidationException(errorMessage);
        }
    }
}
