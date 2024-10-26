package org.example.repository.filerepo;

import org.example.domain.Friendship;
import org.example.domain.validator.Validator;

import java.util.List;

public class FriendshipFileRepo extends AbstractFileRepo<Long, Friendship> {

    public FriendshipFileRepo(Validator<Friendship> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public Friendship extractEntity(List<String> listaInput) {
        Friendship result = new Friendship(Long.parseLong(listaInput.get(1)), Long.parseLong(listaInput.get(2)));
        result.setId(Long.parseLong(listaInput.get(0)));
        return result;
    }

    @Override
    protected String createEntity(Friendship entity) {
        return entity.getId() + ";" + entity.getUser1() + ";" + entity.getUser2();
    }
}
