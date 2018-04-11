/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.newDataStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.oldDataStore.entities.OwnerOld;
import org.springframework.samples.petclinic.oldDataStore.repositories.OwnerRepositoryOld;
import org.springframework.samples.petclinic.newDataStore.owner.Owner;
import org.springframework.samples.petclinic.newDataStore.owner.OwnerRepository;
import org.springframework.samples.petclinic.newDataStore.owner.StaticOwner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

/**
 * @author Gibran
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("junit")
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class NewOwnerStoreTest {

    private static final int TEST_OWNER_ID = 1;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    OwnerRepositoryOld oldOwnerRepository;

    NewOwnerStore testOwnerStore;

    Map<Integer, StaticOwner> ownerStore;

    @Mock
    Owner owner;

    @Before
    public void setup() {
        testOwnerStore = NewOwnerStore.getInstance(ownerRepository);
        testOwnerStore.forklift();
        ownerStore = testOwnerStore.getStore();
        /*
        MockitoAnnotations.initMocks(this);
        when(owner.getId()).thenReturn(TEST_OWNER_ID);
        when(owner.getFirstName()).thenReturn("John");
        when(owner.getLastName()).thenReturn("Doe");
        when(owner.getAddress()).thenReturn("123 Cucumber Lane");
        when(owner.getCity()).thenReturn("Placeville");
        when(owner.getTelephone()).thenReturn("1234567890");
        doReturn("Doe").when(owner).getLastName();
        given(this.ownerRepository.findById(TEST_OWNER_ID)).willReturn(owner);
        */
    }

    @Test
    public void tesOldStore () {
        Collection<OwnerOld> oldData = oldOwnerRepository.findAll();

        Iterator<OwnerOld> iterator = oldData.iterator();
        System.out.println(oldData.size());

        for (Integer id: ownerStore.keySet()){

            if(iterator.hasNext()) {
                OwnerOld oldOwner = iterator.next();
                System.out.println(oldOwner);
            }
        }
    }

    @Test
    public void testForklift() {
        for (Integer id: ownerStore.keySet()){

            Integer key = id;
            String value = ownerStore.get(id).getAddress();
            System.out.println(key + " " + value);
        }
    }

    @Test
    public void testShadowRead() {
        testOwnerStore = NewOwnerStore.getInstance(ownerRepository);
        testOwnerStore.forklift();

        Collection<Owner> results = this.ownerRepository.findByLastName("");

        int inconsistencies = compareResults(results, "");

        assertEquals(inconsistencies, 0);

        // Introduce inconsistency
        testOwnerStore.getStore().put(TEST_OWNER_ID, new StaticOwner(TEST_OWNER_ID, "First", "Last"));
        inconsistencies = compareResults(results, "");
        assertEquals(inconsistencies, 1);


        // Inconsistency should be gone
        inconsistencies = compareResults(results, "");
        assertEquals(inconsistencies, 0);
    }

    private int compareResults(Collection<Owner> results, String lastName) {

        String pattern = "/^" + lastName + "/";

        ArrayList<StaticOwner> staticOwners = new ArrayList<>();

        for (StaticOwner owner : ownerStore.values()) {
            if (!Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(owner.getLastName()).find())
                staticOwners.add(owner);
        }

        System.out.println("Size: " + staticOwners.size());
        int inconsistencies = 0;

        for (Owner owner : results)
        {
            boolean found = false;
            for (StaticOwner staticOwner : staticOwners)
            {
                if (staticOwner.equals(owner)) {
                    System.out.println("Found. Good");
                    found = true;
                    continue;
                }
            }

            if (!found)
            {
                inconsistencies++;
                System.out.println("Not Found. Bad");
                testOwnerStore.findAndReplace(owner);
            }

        }

        return inconsistencies;
    }

    @Test
    public void consistencyCheck () {
        System.out.println(testOwnerStore.checkConsistency());
    }

    //@Test
    public void testShadowWrite() {
        testOwnerStore = NewOwnerStore.getInstance(ownerRepository);

        //make sure that inconsistencies are recorded and fixed
        testOwnerStore.testPutInOldDatastoreOnly(owner);
        assertEquals(1, testOwnerStore.checkConsistency());
        assertEquals(0, testOwnerStore.checkConsistency());

        //make sure that any changes written to old database are also written to new database
        testOwnerStore.save(owner);
        assertEquals(0, testOwnerStore.checkConsistency());
    }
}
