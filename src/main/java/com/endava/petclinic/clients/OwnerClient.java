package com.endava.petclinic.clients;


import com.endava.petclinic.auth.AuthFilter;
import com.endava.petclinic.loggig.LogFilter;
import com.endava.petclinic.models.Owner;
import com.endava.petclinic.models.User;
import com.endava.petclinic.util.EnvReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OwnerClient {
    //nu o facem statica, pt ca vrem sa o instantiem
    //e un api call, specif un request si un response
    public Response createOwner(Owner owner, User user) {
        return given().filters(new AuthFilter(user.getUsername(), user.getPassword()), new LogFilter())
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePathSecured())
                .contentType(ContentType.JSON)
                .body(owner)
                .post(EnvReader.getApiOwners());

    }

    public Response getOwnerById(Integer ownerId, User user) {
        return given().filters(new AuthFilter(user.getUsername(), user.getPassword()), new LogFilter())
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePathSecured())
                .pathParam("ownerId", ownerId)
                .get(EnvReader.getApiOwners() + "/{ownerId}");
    }


}
