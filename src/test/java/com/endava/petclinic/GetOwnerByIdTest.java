package com.endava.petclinic;

import com.endava.petclinic.models.Owner;
import com.endava.petclinic.models.RoleName;
import com.endava.petclinic.models.User;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetOwnerByIdTest extends TestBaseClass{

    @Test
    public void shouldGetOwnerByIdGivenOwnerAdminUser() {
        //behavior driven

        //GIVEN
        //preconditii
        User user = dataGenerator.getUser(RoleName.OWNER_ADMIN);
        Response createUserResponse = userClient.createUser(user);
        createUserResponse.then().statusCode(HttpStatus.SC_CREATED);

        //acum crearea e in preconditii, pt ca nu poti face get daca ownerul nu a fost creat
        Owner owner = dataGenerator.getOwner();
        Response createOwnerResponse = ownerClient.createOwner(owner, user);
        createOwnerResponse.then().statusCode(HttpStatus.SC_CREATED);

        //WHEN
        //actiunea, ar trebui sa fie una singura
        Response getOwnerByIdResponse = ownerClient.getOwnerById(createOwnerResponse.jsonPath().getInt("id"),user);

        //THEN
        //validarile
        getOwnerByIdResponse.then().statusCode(HttpStatus.SC_OK);
        Owner owner1 = getOwnerByIdResponse.as(Owner.class);
        assertThat(owner1, is(owner));

    }

    @Test
    public void shouldGetOwnerByIdGivenOwnerAdminUserWithAnotherLayer() {
        //behavior driven

        //GIVEN
        //preconditii
        //business layer - grupare de metode tehnice
        petClinicFeature.createUser(RoleName.OWNER_ADMIN)
                .createOwner();
        Owner owner = petClinicFeature.getOwner();
        User user = petClinicFeature.getUser();

        //WHEN
        //actiunea, ar trebui sa fie una singura
        Response getOwnerByIdResponse = ownerClient.getOwnerById(owner.getId(),user);

        //THEN
        //validarile
        getOwnerByIdResponse.then().statusCode(HttpStatus.SC_OK);
        Owner owner1 = getOwnerByIdResponse.as(Owner.class);
        assertThat(owner1, is(owner));

    }

}
