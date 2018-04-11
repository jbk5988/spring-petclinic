/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.migrate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.newDataStore.owner.Owner;
import org.springframework.samples.petclinic.newDataStore.owner.OwnerRepository;
import org.springframework.samples.petclinic.oldDataStore.entities.OwnerOld;
import org.springframework.samples.petclinic.oldDataStore.repositories.OwnerRepositoryOld;
import org.springframework.scheduling.annotation.Async;

/**
 * @author Gibran
 */
public class Shadow {

    @Autowired
    private ConsistencyCheck consistencyCheck;

    @Autowired
    private OwnerRepository currentOwnerRepo;

    @Autowired
    private OwnerRepositoryOld oldOwnerRepo;

    @Async("ShadowWriteThread")
    public void save(OwnerOld old) {
        System.out.println("Shadow writing to old data store");
        currentOwnerRepo.save(convertOwnerOldToOwner(old));
        consistencyCheck.shadowWriteCheck(old);
    }

    @Async("ShadowReadThread")
    public void findOwnerById(OwnerOld old, int id){
        System.out.println("Shadow reading from old data store");
        Owner currentOwner = currentOwnerRepo.findById(id);
        consistencyCheck.shadowReadCheck(old, currentOwner);
    }

    public Owner convertOwnerOldToOwner(OwnerOld oldOwner) {
        Owner owner = new Owner();
        owner.setId(oldOwner.getId());
        owner.setFirstName(oldOwner.getFirstName());
        owner.setLastName(oldOwner.getLastName());
        owner.setAddress(oldOwner.getAddress());
        owner.setCity(oldOwner.getCity());
        owner.setTelephone(oldOwner.getTelephone());

//        for(PetOld pet : oldOwner.getPets()){
//            System.out.println("Pets are " + pet);
//            Pet migratePet = convertPetOldToPet(pet);
//            migratePet.setOwner(owner);
//            owner.addPet(migratePet);
//        }
        return owner;
    }
}
