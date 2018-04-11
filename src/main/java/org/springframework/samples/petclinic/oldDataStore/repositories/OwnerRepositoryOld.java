/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.oldDataStore.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.oldDataStore.entities.OwnerOld;

/**
 * @author Gibran
 */
public interface OwnerRepositoryOld extends Repository<OwnerOld, Integer> {

    /**
     * Retrieve {@link OwnerOld}s from the old data store by last name, returning all owners
     * whose last name <i>starts</i> with the given name.
     * @param lastName Value to search for
     * @return a Collection of matching {@link OwnerOld}s (or an empty Collection if none
     * found)
     */
    @Query("SELECT DISTINCT owner FROM OwnerOld owner left join fetch owner.pets WHERE owner.lastName LIKE :lastName%")
    @Transactional(readOnly = true)
    Collection<OwnerOld> findByLastName(@Param("lastName") String lastName);

    /**
     * Retrieve an {@link OwnerOld} from the old data store by id.
     * @param id the id to search for
     * @return the {@link OwnerOld} if found
     */
    @Query("SELECT owner FROM OwnerOld owner left join fetch owner.pets WHERE owner.id =:id")
    @Transactional(readOnly = true)
    OwnerOld findById(@Param("id") Integer id);

    /**
     * Retrieve all <code>OwnerOld</code>s from the old data store.
     *
     * @return a <code>Collection</code> of <code>OwnerOld</code>s
     */
    @Transactional(readOnly = true)
    @Cacheable("owner")
    Collection<OwnerOld> findAll() throws DataAccessException;

    /**
     * Save an {@link OwnerOld} to the old data store, either inserting or updating it.
     * @param owner the {@link OwnerOld} to save
     */
    void save(OwnerOld owner);


}
