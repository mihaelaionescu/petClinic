package com.endava.petclinic;

import com.endava.petclinic.models.Owner;
import com.endava.petclinic.models.RoleName;
import com.endava.petclinic.models.User;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static com.endava.petclinic.models.RoleName.OWNER_ADMIN;
import static org.hamcrest.Matchers.*;

public class CreateOwnerTest extends TestBaseClass {

    @Test
    //shouldAction_givenPrecondition_
    //ar trebui sa creez un owner daca am un admin de tip owner
    public void shouldCreateOwnerGivenOwnerAdminUser() {
        //behavior driven

        //GIVEN
        //preconditii
        User user = dataGenerator.getUser(OWNER_ADMIN);
        Response createUserResponse = userClient.createUser(user);
        createUserResponse.then().statusCode(HttpStatus.SC_CREATED);

        Owner owner = dataGenerator.getOwner();

        //WHEN
        //actiunea, ar trebui sa fie una singura
        Response createOwnerResponse = ownerClient.createOwner(owner, user);

        //THEN
        //validarile
        createOwnerResponse.then().statusCode(HttpStatus.SC_CREATED)
                .body("id", not(nullValue())) //validare sa nu fie id-ul null
                .body("firstName", is(owner.getFirstName()));
    }

    @Test
    public void shouldFailToCreateOwnerGivenVetAdminUser(){
        //GIVEN
        //preconditii
        User user = dataGenerator.getUser(RoleName.VET_ADMIN);
        Response createUserResponse = userClient.createUser(user);
        createUserResponse.then().statusCode(HttpStatus.SC_CREATED);

        Owner owner = dataGenerator.getOwner();

        //WHEN
        //actiunea, ar trebui sa fie una singura
        Response createOwnerResponse = ownerClient.createOwner(owner, user);

        //THEN
        //validarile
        //403 -- ca nu am rolul necesar, eroare de autorizare.. aici e un bug pt ca da eroare de bad_request
        createOwnerResponse.then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

}
