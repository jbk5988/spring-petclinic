/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.migrate;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.newDataStore.owner.Owner;
import org.springframework.samples.petclinic.newDataStore.owner.OwnerRepository;
import org.springframework.samples.petclinic.oldDataStore.entities.OwnerOld;
import org.springframework.samples.petclinic.oldDataStore.repositories.OwnerRepositoryOld;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Gibran
 */
public class ConsistencyCheck {

    private int inconsistencyCount = 0;

    @Autowired
    private OwnerRepository currentOwnerRepo;

    @Autowired
    private OwnerRepositoryOld oldOwnerRepo;

    private static boolean forkLifted = false;

    public static void forkLifted() {
        forkLifted = true;
    }

    @Scheduled(cron = "*/30 * * * * *")
    @Async("ConsistencyCheckThread")
    public void check(){
        if(forkLifted) {
            System.out.println("Checking consistency");
            inconsistencyCount += checkOwners();
        }
    }

    private int checkOwners(){
        int inconsistencyLocalCount = 0;

        Map<Integer, OwnerOld> oldData = oldOwnerRepo.findAll().stream()
            .collect(Collectors.toMap(owner -> owner.getId(), owner -> owner));

        Map<Integer, Owner> newData = currentOwnerRepo.findAll().stream()
            .collect(Collectors.toMap(owner -> owner.getId(), owner -> owner));


        for(Integer oldKey : oldData.keySet()){
            OwnerOld oldOwner = oldData.get(oldKey);
            Owner currentOwner = newData.get(oldKey);

            if(!isConsistent(oldOwner, currentOwner)){
                inconsistencyLocalCount ++;
                System.out.println("Inconsistency found");
                currentOwnerRepo.save(convertOwnerOldToOwner(oldOwner));
            }
        }
        System.out.println("No inconsistencies");
        return inconsistencyLocalCount;
    }

    public boolean isConsistent(OwnerOld oldOwner, Owner currentOwner){
        System.out.println("Old Owner:" + oldOwner.getId());
        System.out.println("Current Owner:" + currentOwner.getId());

        if( oldOwner.getId() == currentOwner.getId()
            && oldOwner.getLastName().equals(currentOwner.getLastName())
            && oldOwner.getFirstName().equals(currentOwner.getFirstName())
            && oldOwner.getAddress().equals(currentOwner.getAddress())
            && oldOwner.getCity().equals(currentOwner.getCity())
            && oldOwner.getTelephone().equals(currentOwner.getTelephone())
        ) {
            return true;
        }
        else return false;
    }

    public boolean shadowWriteCheck(OwnerOld owner){
        System.out.println("Checking shadow write consistency");
        OwnerOld oldOwner = oldOwnerRepo.findById(owner.getId());
        Owner currentOwner = currentOwnerRepo.findById(owner.getId());

        return isConsistent(oldOwner, currentOwner);
    }

    public int shadowReadCheck(OwnerOld old, Owner current) {
        System.out.println("Checking shadow read consistency");
        int inconsistencyLocalCount = 0;

        if(!isConsistent(old, current)) {
            inconsistencyLocalCount++;
            System.out.println("Inconsistency found");
            currentOwnerRepo.deleteById(current.getId());
            currentOwnerRepo.save(convertOwnerOldToOwner(old));
        }

        return inconsistencyLocalCount;
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
