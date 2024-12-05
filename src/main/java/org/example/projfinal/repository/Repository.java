package org.example.projfinal.repository;

import java.util.Optional;

import org.example.projfinal.domain.Entity;
import org.example.projfinal.repository.filters.Filter;
import org.example.projfinal.utils.paging.Page;
import org.example.projfinal.utils.paging.Pageable;

public interface Repository<ID, E extends Entity<ID>> {
    /**
     * @param id -the id of the entity to be returned
     *           <p>
     *           id must not be null
     * @return an {@code Optional} encapsulating the entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    Optional<E> findOne(ID id);

    /**
     * @return all entities
     */
    Iterable<E> findAll();

    /**
     * @param entity entity must be not null
     * @return an {@code Optional} - null if the entity was saved,
     * <p>
     * - the entity (id already exists)
     * @throws org.example.projfinal.domain.validator.ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.
     */
    Optional<E> save(E entity);

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return an {@code Optional}
     * <p>
     * - null if there is no entity with the given id,
     * <p>
     * - the removed entity, otherwise
     * @throws IllegalArgumentException if the given id is null.
     */
    Optional<E> delete(ID id);

    /**
     * * @param entity
     * <p>
     * entity must not be null
     *
     * @return an {@code Optional}
     * <p>
     * - null if the entity was updated
     * <p>
     * - otherwise (e.g. id does not exist) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws org.example.projfinal.domain.validator.ValidationException      if the entity is not valid
     */
    Optional<E> update(E entity);

    Page<E> findAllOnPage(Pageable pageable, Filter filter);
}