package com.endava.petclinic.clients;

import com.endava.petclinic.auth.AuthFilter;
import com.endava.petclinic.loggig.LogFilter;
import com.endava.petclinic.models.User;
import com.endava.petclinic.util.EnvReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {

    public Response createUser(User user) {
        return given().filters(new AuthFilter(EnvReader.getAdminUsername(), EnvReader.getAdminPassword()), new LogFilter())
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePathSecured())
                .contentType(ContentType.JSON)
                .body(user)
                .post(EnvReader.getApiUsers());
    }
}
