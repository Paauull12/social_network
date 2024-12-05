package org.example.projfinal.domain.validator;


import org.example.projfinal.domain.Message;

public class MessageValidation implements Validator<Message> {

    @Override
    public void validate(Message entity) throws ValidationException {
        String errorMessage = "";

        if (entity.getMessage() == null || entity.getMessage().isEmpty()) {
            errorMessage += "Message text cannot be empty!";
        }
        if (entity.getUserId() == null) {
            errorMessage += "User id cannot be null!";
        }
        if (entity.getDate() == null) {
            errorMessage += "Date cannot be null!";
        }
        if (entity.getTypeMsg() == null) {
            errorMessage += "Message type cannot be null!";
        }

        if (!errorMessage.isEmpty()) {
            throw new ValidationException(errorMessage);
        }
    }

}
