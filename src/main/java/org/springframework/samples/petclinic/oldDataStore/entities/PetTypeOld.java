/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.oldDataStore.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.samples.petclinic.oldDataStore.model.NamedEntity;

/**
 * @author Gibran
 */
@Entity
@Table(name = "types")
public class PetTypeOld extends NamedEntity {

}
