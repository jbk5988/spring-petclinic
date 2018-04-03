package org.springframework.samples.petclinic.owner;
import java.util.ArrayList;

public class OwnerStorage {

	ArrayList<Owner> ownerList = new ArrayList<Owner>();
	
 	public void forklift() {

 	}

 	public int checkConsistency() {
 		int inconsistencies = 0;
 		
 		for(int i=0; i< ownerList.size(); i++)
 		{
 	//	Owner expected = 
 		Owner actual = ownerList.get(i);
 		
 		if(!expected.equals(actual)) {
 			{
 				ownerList.set(i, expected);
 				
 				inconsistencies++;
 				violation(i, expected, actual);
 			}
 		}
 		}
 		
 		return inconsistencies;
 	}

 	private void violation(int i, Owner expected, Owner actual)
 	{
 		System.out.println("Consistency Violation! \n" + "i = " + i + "\n\t expected = " + 
 				expected.toString() + "\n\t actual = " + actual.toString());
 	}
}
