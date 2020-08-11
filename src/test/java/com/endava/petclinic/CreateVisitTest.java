package com.endava.petclinic;

import com.endava.petclinic.models.RoleName;
import com.endava.petclinic.models.User;
import com.endava.petclinic.models.Visit;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class CreateVisitTest extends TestBaseClass{

    @Test
    public void shouldCreateVisitGivenVisitUser(){
        //GIVEN
        petClinicFeature.createUser(RoleName.OWNER_ADMIN)
                .createOwner()
                .createType()
                .createPet()
                .createVisit();

        User user = petClinicFeature.getUser();
        Visit visit = petClinicFeature.getVisit();

        //WHEN
        Response createVisit = visitClient.createVisit(visit, user);

        //THEN
        createVisit.then().statusCode(HttpStatus.SC_CREATED)
                .body("id", not(nullValue()));
    }
}
