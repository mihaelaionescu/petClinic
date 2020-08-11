package com.endava.petclinic;

import com.endava.petclinic.models.*;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class CreatePetTest extends TestBaseClass{

    @Test
    public void shouldCreatePetGivenOwnerPetType(){
        //GIVEN
        petClinicFeature.createUser(RoleName.OWNER_ADMIN)
                .createOwner()
                .createType()
                .createPet();
        User user = petClinicFeature.getUser();
        Pet pet = petClinicFeature.getPet();

        //WHEN
        Response createPet = petClient.createPet(pet, user);

        //THEN
        createPet.then().statusCode(HttpStatus.SC_CREATED)
                .body("id", not(nullValue()));
    }

}
