package org.springframework.samples.petclinic.teamkoganM2additions;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.Test;
import org.springframework.samples.petclinic.newDataStore.owner.Pet;
import org.springframework.samples.petclinic.newDataStore.owner.PetRepository;
import org.springframework.samples.petclinic.newDataStore.owner.VisitController;
import org.springframework.samples.petclinic.newDataStore.visit.Visit;
import org.springframework.samples.petclinic.newDataStore.visit.VisitRepository;

public class VisitControllerTest
{
    @Test
    public void testPetVisitDate()
    {
		// Define random pet ID
		final Integer PET_ID = 7;
		Pet pet7 = new Pet();

		// Mock dependencies
		VisitRepository mockVisitRepo = mock(VisitRepository.class);
		PetRepository mockPetRepo = mock(PetRepository.class);
		VisitController visitController = new VisitController(mockVisitRepo, mockPetRepo);

		// Define mock behaviour
		when(mockPetRepo.findById(PET_ID)).thenReturn(pet7);

		// Call method under inspection
		Visit visit = visitController.loadPetWithVisit(PET_ID, new HashMap<>());

		// Confirm that the same visit date was assigned to the pet
		assertEquals(visit.getDate(), pet7.getVisits().get(0).getDate());
    }
}
