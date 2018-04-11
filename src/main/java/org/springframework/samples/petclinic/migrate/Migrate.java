/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.migrate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Gibran
 */
@Component
public class Migrate {

    @Autowired
    private ForkLift forkLift;

    public void run() {
        forkLift.forkLiftOldOwners();
        ConsistencyCheck.forkLifted();
    }
}
