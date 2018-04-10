/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.oldDataStore.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.NamedEntity;

/**
 * @author Gibran
 */
@Entity
@Table(name = "pets")
public class PetOld extends NamedEntity {

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private PetTypeOld type;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private OwnerOld owner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "petId", fetch = FetchType.EAGER)
    private Set<VisitOld> visits = new LinkedHashSet<>();

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getBirthDate() {
        return this.birthDate;
    }

    public PetTypeOld getType() {
        return this.type;
    }

    public void setType(PetTypeOld type) {
        this.type = type;
    }

    public OwnerOld getOwner() {
        return this.owner;
    }

    protected void setOwner(OwnerOld owner) {
        this.owner = owner;
    }

    protected Set<VisitOld> getVisitsInternal() {
        if (this.visits == null) {
            this.visits = new HashSet<>();
        }
        return this.visits;
    }

    protected void setVisitsInternal(Set<VisitOld> visits) {
        this.visits = visits;
    }

    public List<VisitOld> getVisits() {
        List<VisitOld> sortedVisits = new ArrayList<>(getVisitsInternal());
        PropertyComparator.sort(sortedVisits,
            new MutableSortDefinition("date", false, false));
        return Collections.unmodifiableList(sortedVisits);
    }

    public void addVisit(VisitOld visit) {
        getVisitsInternal().add(visit);
        visit.setPetId(this.getId());
    }

}
