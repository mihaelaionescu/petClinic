package com.endava.petclinic;

import com.endava.petclinic.clients.OwnerClient;
import com.endava.petclinic.clients.UserClient;
import com.endava.petclinic.data.DataGenerator;
import com.endava.petclinic.fixture.PetClinicFixture;

public class TestBaseClass {
    protected OwnerClient ownerClient = new OwnerClient();
    protected UserClient userClient = new UserClient();
    protected DataGenerator dataGenerator = new DataGenerator();
    protected PetClinicFixture petClinicFeature= new PetClinicFixture();
}
