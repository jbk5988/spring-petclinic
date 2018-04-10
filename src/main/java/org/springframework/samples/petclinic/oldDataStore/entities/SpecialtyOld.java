/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.oldDataStore.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.samples.petclinic.model.NamedEntity;

/**
 * @author Gibran
 */
@Entity
@Table(name = "specialties")
public class SpecialtyOld extends NamedEntity implements Serializable {

}
