package com.endava.petclinic.fixture;

import com.endava.petclinic.clients.*;
import com.endava.petclinic.data.DataGenerator;
import com.endava.petclinic.models.*;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

public class PetClinicFixture {
    private DataGenerator dataGenerator = new DataGenerator();
    private UserClient userClient = new UserClient();
    private OwnerClient ownerClient = new OwnerClient();
    private TypeClient typeClient = new TypeClient();
    private PetClient petClient = new PetClient();
    private VisitClient visitClient = new VisitClient();

    private User user;
    private Owner owner;
    private Type type;
    private Pet pet;
    private Visit visit;

    //facem sa intoarca un obiect de tipul clasei, ca sa putem inlantui metodele
    public PetClinicFixture createUser(RoleName... roleNames) {

        user = dataGenerator.getUser(roleNames);
        Response response = userClient.createUser(user);
        response.then().statusCode(HttpStatus.SC_CREATED);

        return this;
    }

    public PetClinicFixture createOwner() {
        owner = dataGenerator.getOwner();
        Response createOwnerResponse = ownerClient.createOwner(owner, user);
        createOwnerResponse.then().statusCode(HttpStatus.SC_CREATED);

        owner.setId(createOwnerResponse.jsonPath().getInt("id"));

        return this;
    }

    public PetClinicFixture createType() {
        type = dataGenerator.getType();
        Response createType = typeClient.createType(type, user);
        createType.then().statusCode(HttpStatus.SC_CREATED);

        type.setId(createType.jsonPath().getInt("id"));

        return this;
    }

    public PetClinicFixture createPet(){
        pet = dataGenerator.getPet(owner, type);
        Response createPet = petClient.createPet(pet, user);
        createPet.then().statusCode(HttpStatus.SC_CREATED);

        pet.setId(createPet.jsonPath().getInt("id"));

        return this;
    }

    public PetClinicFixture createVisit(){
        visit = dataGenerator.getVisit(pet);
        Response createVisit = visitClient.createVisit(visit, user);
        createVisit.then().statusCode(HttpStatus.SC_CREATED);

        visit.setId(createVisit.jsonPath().getInt("id"));

        return this;
    }

    public User getUser() {
        return user;
    }

    public Owner getOwner() {
        return owner;
    }

    public Type getType() {
        return type;
    }

    public Pet getPet() { return pet; }

    public Visit getVisit() { return visit; }

}
