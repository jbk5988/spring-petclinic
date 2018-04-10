/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.oldDataStore.repositories;

import java.util.Collection;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.oldDataStore.entities.VetOld;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Gibran
 */
public interface VetRepositoryOld extends Repository<VetOld, Integer> {

    /**
     * Retrieve all <code>VetOld</code>s from the old data store.
     *
     * @return a <code>Collection</code> of <code>VetOld</code>s
     */
    @Transactional(readOnly = true)
    @Cacheable("vets")
    Collection<VetOld> findAll() throws DataAccessException;


}
