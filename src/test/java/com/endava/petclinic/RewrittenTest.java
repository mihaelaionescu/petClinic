package com.endava.petclinic;

import com.endava.petclinic.clients.OwnerClient;
import com.endava.petclinic.clients.UserClient;
import com.endava.petclinic.data.DataGenerator;
import com.endava.petclinic.models.Owner;
import com.endava.petclinic.models.RoleName;
import com.endava.petclinic.models.User;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

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


}
