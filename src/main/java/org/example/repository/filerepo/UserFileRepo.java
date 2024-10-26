package org.example.repository.filerepo;

import org.example.domain.User;
import org.example.domain.validator.Validator;

import java.util.List;

public class UserFileRepo extends AbstractFileRepo<Long, User> {

    public UserFileRepo(Validator<User> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public User extractEntity(List<String> listaInput) {
        User entity = new User(listaInput.get(1), listaInput.get(2));
        entity.setId(Long.parseLong(listaInput.get(0)));
        return entity;
    }

    @Override
    protected String createEntity(User entity) {
        return entity.getId() + ";" + entity.getUsername() + ";" + entity.getRealName();
    }
}
