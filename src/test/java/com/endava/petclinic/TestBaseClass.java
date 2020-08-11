package com.endava.petclinic;

import com.endava.petclinic.clients.*;
import com.endava.petclinic.data.DataGenerator;
import com.endava.petclinic.fixture.PetClinicFixture;

public class TestBaseClass {
    protected OwnerClient ownerClient = new OwnerClient();
    protected UserClient userClient = new UserClient();
    protected TypeClient typeClient = new TypeClient();
    protected PetClient petClient = new PetClient();
    protected VisitClient visitClient = new VisitClient();
    protected DataGenerator dataGenerator = new DataGenerator();
    protected PetClinicFixture petClinicFeature= new PetClinicFixture();
}
