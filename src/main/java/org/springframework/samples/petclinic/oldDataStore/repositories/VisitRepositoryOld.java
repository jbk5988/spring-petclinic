/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.oldDataStore.repositories;

import java.util.Collection;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.oldDataStore.entities.VisitOld;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Gibran
 */
public interface VisitRepositoryOld extends Repository<VisitOld, Integer> {

    /**
     * Save a <code>VisitOld</code> to the old data store, either inserting or updating it.
     *
     * @param visit the <code>VisitOld</code> to save
     * @see BaseEntity#isNew
     */
    void save(VisitOld visit) throws DataAccessException;

    List<VisitOld> findByPetId(Integer petId);

    /**
     * Retrieve all <code>VisitOld</code>s from the data store.
     *
     * @return a <code>VisitOld</code> of <code>Owner</code>s
     */
    @Transactional(readOnly = true)
    Collection<VisitOld> findAll() throws DataAccessException;
}
