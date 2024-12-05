package org.example.projfinal.domain.validator;

import org.example.projfinal.domain.Group;

public class GroupValidation implements Validator<Group> {
    @Override
    public void validate(Group entity) throws ValidationException {
        if (entity.getGroupName() == null || entity.getGroupName().isEmpty()) {
            throw new ValidationException("Group name cannot be empty");
        }
    }
}
