/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.oldDataStore.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.samples.petclinic.oldDataStore.model.Person;
/**
 * @author Gibran
 */
@Entity
@Table(name = "vets")
public class VetOld extends Person {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "vet_specialties", joinColumns = @JoinColumn(name = "vet_id"), inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private Set<SpecialtyOld> specialties;

    protected Set<SpecialtyOld> getSpecialtiesInternal() {
        if (this.specialties == null) {
            this.specialties = new HashSet<>();
        }
        return this.specialties;
    }

    protected void setSpecialtiesInternal(Set<SpecialtyOld> specialties) {
        this.specialties = specialties;
    }

    @XmlElement
    public List<SpecialtyOld> getSpecialties() {
        List<SpecialtyOld> sortedSpecs = new ArrayList<>(getSpecialtiesInternal());
        PropertyComparator.sort(sortedSpecs,
            new MutableSortDefinition("name", true, true));
        return Collections.unmodifiableList(sortedSpecs);
    }

    public int getNrOfSpecialties() {
        return getSpecialtiesInternal().size();
    }

    public void addSpecialty(SpecialtyOld specialty) {
        getSpecialtiesInternal().add(specialty);
    }

}
