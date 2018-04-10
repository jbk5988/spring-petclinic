/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.migrate;

import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.oldDataStore.entities.OwnerOld;
import org.springframework.samples.petclinic.oldDataStore.entities.PetOld;
import org.springframework.samples.petclinic.oldDataStore.entities.PetTypeOld;
import org.springframework.samples.petclinic.oldDataStore.entities.SpecialtyOld;
import org.springframework.samples.petclinic.oldDataStore.entities.VetOld;
import org.springframework.samples.petclinic.oldDataStore.entities.VisitOld;
import org.springframework.samples.petclinic.oldDataStore.repositories.OwnerRepositoryOld;
import org.springframework.samples.petclinic.oldDataStore.repositories.PetRepositoryOld;
import org.springframework.samples.petclinic.oldDataStore.repositories.VetRepositoryOld;
import org.springframework.samples.petclinic.oldDataStore.repositories.VisitRepositoryOld;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;

/**
 * @author Gibran
 */
public class ForkLift {

    @Autowired
    private OwnerRepository currentOwnerRepo;
    @Autowired
    private PetRepository currentPetRepo;
    @Autowired
    private VetRepository currentVetRepo;
    @Autowired
    private VisitRepository currentVisitRepo;

    @Autowired
    private OwnerRepositoryOld oldOwnerRepo;
    @Autowired
    private PetRepositoryOld oldPetRepo;
    @Autowired
    private VetRepositoryOld oldVetRepo;
    @Autowired
    private VisitRepositoryOld oldVisitRepo;

    public void forkLiftAll() {
        forkLiftOldOwners();
        forkLiftOldVisits();
        forkLiftOldPets();
        forkLiftOldVets();
    }

    public void forkLiftOldOwners() {

        currentOwnerRepo.deleteAll();
        Collection<OwnerOld> oldData = oldOwnerRepo.findAll();

        for (OwnerOld owner : oldData) {
            Owner migratedOwner = convertOwnerOldToOwner(owner);
            currentOwnerRepo.save(migratedOwner);
        }
    }

    public void forkLiftOldPets() {
        currentPetRepo.deleteAll();
        Collection<PetOld> oldData = oldPetRepo.findAll();

        for (PetOld pet : oldData) {
            Pet migratedPet = convertPetOldToPet(pet);
            Owner migratedOwner = convertOwnerOldToOwner(pet.getOwner());
            migratedPet.setOwner(migratedOwner);
            currentPetRepo.save(migratedPet);
        }
    }

    public void forkLiftOldVets() {
        currentVetRepo.deleteAll();
        Collection<VetOld> oldData = oldVetRepo.findAll();

        for(VetOld vet: oldData){
            Vet migratedVet = convertVetOldToVet(vet);
            currentVetRepo.save(migratedVet);
        }


    }

    public void forkLiftOldVisits() {
        currentVisitRepo.deleteAll();
        Collection<VisitOld> oldData = oldVisitRepo.findAll();

        for (VisitOld visit : oldData) {
            Visit migratedVisit = convertVisitOldToVisit(visit);
            currentVisitRepo.save(migratedVisit);
        }
    }

    public Owner convertOwnerOldToOwner(OwnerOld oldOwner) {
        Owner owner = new Owner();
        owner.setId(oldOwner.getId());
        owner.setFirstName(oldOwner.getFirstName());
        owner.setLastName(oldOwner.getLastName());
        owner.setAddress(oldOwner.getAddress());
        owner.setCity(oldOwner.getCity());
        owner.setTelephone(oldOwner.getTelephone());

        for(PetOld pet : oldOwner.getPets()){
            Pet migratePet = convertPetOldToPet(pet);
            migratePet.setOwner(owner);
            owner.addPet(migratePet);
        }
        return owner;
    }

    public Pet convertPetOldToPet(PetOld oldPet) {
        Pet pet = new Pet();
        pet.setBirthDate(oldPet.getBirthDate());
        pet.setType(convertOldTypeToType(oldPet.getType()));
        pet.setId(oldPet.getId());
        pet.setName(oldPet.getName());

        return pet;
    }

    public PetType convertOldTypeToType(PetTypeOld oldPetType) {
        PetType petType = new PetType();
        petType.setId(oldPetType.getId());
        petType.setName(oldPetType.getName());

        return petType;
    }

    public Vet convertVetOldToVet(VetOld oldVet) {
        Vet vet = new Vet();
        vet.setId(oldVet.getId());
        vet.setFirstName(oldVet.getFirstName());
        vet.setLastName(oldVet.getLastName());

        List<SpecialtyOld> oldSpecialties = oldVet.getSpecialties();

        if (!oldSpecialties.isEmpty()) {
            for (SpecialtyOld specialty : oldSpecialties) {
                Specialty migratedSpecialty = new Specialty();
                migratedSpecialty.setName(specialty.getName());
                vet.addSpecialty(migratedSpecialty);
            }
        }
        return vet;
    }

    public Visit convertVisitOldToVisit(VisitOld oldVisit) {
        Visit visit = new Visit();
        visit.setDate(oldVisit.getDate());
        visit.setDescription(oldVisit.getDescription());
        visit.setPetId(oldVisit.getPetId());
        return visit;
    }
}
