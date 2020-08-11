package com.endava.petclinic;

import com.endava.petclinic.clients.OwnerClient;
import com.endava.petclinic.clients.UserClient;
import com.endava.petclinic.data.DataGenerator;
import com.endava.petclinic.models.*;
import com.endava.petclinic.util.EnvReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RewrittenTest extends TestBaseClass{

    @Test
    public void firstApiCall() {
        //create new user
        User user = dataGenerator.getUser(RoleName.OWNER_ADMIN);
        //create user
        //-------------------
        Response createUserResponse = userClient.createUser(user);
        createUserResponse.then().statusCode(HttpStatus.SC_CREATED);

        //create new owner
        Owner owner = dataGenerator.getOwner();
        //create owner
        //---------------------
        Response response = ownerClient.createOwner(owner, user);
        response.then().statusCode(HttpStatus.SC_CREATED);

        Integer id = response.jsonPath().getInt("id");


        //get owner by id
        //--------------------
        Response getResponse = ownerClient.getOwnerById(id, user);
        getResponse.then().statusCode(HttpStatus.SC_OK);

        Owner actualOwner = getResponse.as(Owner.class);


        assertThat(actualOwner, is(owner));
    }

    @Test
    public void addPet(){
        User user = dataGenerator.getUser(RoleName.OWNER_ADMIN);
        //create user
        //-------------------
        Response createUserResponse = userClient.createUser(user);
        createUserResponse.then().statusCode(HttpStatus.SC_CREATED);

        //create owner
        Owner owner = dataGenerator.getOwner();
        Response createOwner = ownerClient.createOwner(owner,user);
        createOwner.then().statusCode(HttpStatus.SC_CREATED);
        owner.setId(createOwner.jsonPath().getInt("id"));

        //create a type
        Type type = dataGenerator.getType();
        Response createType = typeClient.createType(type, user);
        createType.then().statusCode(HttpStatus.SC_CREATED);
        type.setId(createType.jsonPath().getInt("id"));

        //create pet
        Pet pet = dataGenerator.getPet(owner, type);
        Response createPet = petClient.createPet(pet, user);
        createPet.then().statusCode(HttpStatus.SC_CREATED);
        pet.setId(createPet.jsonPath().getInt("id"));

        //get owner to see that pet was added
        Response getPet = petClient.getPet(pet.getId(), user);
        getPet.then().statusCode(HttpStatus.SC_OK);
        Pet actualPet = getPet.as(Pet.class);

        assertThat(actualPet, is(pet));



    }

    @Test
    public void addVisit(){

        User user = dataGenerator.getUser(RoleName.OWNER_ADMIN);
        //create user
        //-------------------
        Response createUserResponse = userClient.createUser(user);
        createUserResponse.then().statusCode(HttpStatus.SC_CREATED);

        Owner owner = dataGenerator.getOwner();
        Response createOwner = ownerClient.createOwner(owner, user);
        createOwner.then().statusCode(HttpStatus.SC_CREATED);
        owner.setId(createOwner.jsonPath().getInt("id"));

        Type type = dataGenerator.getType();
        Response createType = typeClient.createType(type, user);
        createType.then().statusCode(HttpStatus.SC_CREATED);
        type.setId(createType.jsonPath().getInt("id"));

        Pet pet = dataGenerator.getPet(owner, type);
        Response createPet = petClient.createPet(pet, user);
        createPet.then().statusCode(HttpStatus.SC_CREATED);
        pet.setId(createPet.jsonPath().getInt("id"));

        //returneaza o data in trecut cel mult 10 zile
        Visit visit = dataGenerator.getVisit(pet);
        Response createVisit = visitClient.createVisit(visit, user);
        createVisit.then().statusCode(HttpStatus.SC_CREATED);
        visit.setId(createVisit.jsonPath().getInt("id"));

        Visit actualVisit = createVisit.as(Visit.class);

        assertThat(actualVisit, is(visit));

    }


}
