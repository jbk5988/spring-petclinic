/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.oldDataStore.repositories;

import java.util.Collection;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.oldDataStore.entities.PetOld;
import org.springframework.samples.petclinic.oldDataStore.entities.PetTypeOld;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Gibran
 */
public interface PetRepositoryOld extends Repository<PetOld, Integer> {

    /**
     * Retrieve all {@link PetTypeOld}s from the data store.
     * @return a Collection of {@link PetTypeOld}s.
     */
    @Query("SELECT ptype FROM PetTypeOld ptype ORDER BY ptype.name")
    @Transactional(readOnly = true)
    List<PetTypeOld> findPetTypes();

    /**
     * Retrieve a {@link PetOld} from the old data store by id.
     * @param id the id to search for
     * @return the {@link PetOld} if found
     */
    @Transactional(readOnly = true)
    PetOld findById(Integer id);

    /**
     * Save a {@link PetOld} to the old data store, either inserting or updating it.
     * @param pet the {@link PetOld} to save
     */
    void save(PetOld pet);

    /**
     * Retrieve all <code>PetOld</code>s from the data store.
     *
     * @return a <code>PetOld</code> of <code>PetOld</code>s
     */
    @Transactional(readOnly = true)
    Collection<PetOld> findAll() throws DataAccessException;
}

