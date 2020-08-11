package com.endava.petclinic.clients;

import com.endava.petclinic.auth.AuthFilter;
import com.endava.petclinic.loggig.LogFilter;
import com.endava.petclinic.models.Type;
import com.endava.petclinic.models.User;
import com.endava.petclinic.util.EnvReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class TypeClient {
    public Response createType(Type typePet, User user){
        return given().filters(new AuthFilter(user.getUsername(), user.getPassword()), new LogFilter())
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .contentType(ContentType.JSON)
                .body(typePet)
                .post(EnvReader.getApiPetTypes());
    }
}
