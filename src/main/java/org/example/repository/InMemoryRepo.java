package org.example.repository;

import org.example.domain.Entity;
import org.example.domain.validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepo<ID, E extends Entity<ID>> implements Repository<ID, E> {

    protected Map<ID, E> lista;
    private final Validator<E> validator;

    public InMemoryRepo(Validator<E> validator) {
        this.validator = validator;
        lista = new HashMap<>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        if(id == null)
            throw new IllegalArgumentException("id must be not null");
        return Optional.ofNullable(lista.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return lista.values();
    }

    @Override
    public Optional<E> save(E entity) {
        if(entity == null){
            throw new IllegalArgumentException("entity must be not null");
        }
        validator.validate(entity);
        return Optional.ofNullable(lista.put(entity.getId(), entity));
    }

    @Override
    public Optional<E> delete(ID id) {
        if(id == null){
            throw new IllegalArgumentException("id must be not null");
        }
        return Optional.ofNullable(lista.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        if(entity == null){
            throw new IllegalArgumentException("entity must be not null");
        }
        validator.validate(entity);

        return lista.get(entity.getId()) != null ? Optional.empty() : Optional.of(entity);
    }
}
