package com.endava.petclinic.clients;

import com.endava.petclinic.auth.AuthFilter;
import com.endava.petclinic.loggig.LogFilter;
import com.endava.petclinic.models.Pet;
import com.endava.petclinic.models.User;
import com.endava.petclinic.util.EnvReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class PetClient {

    public Response createPet(Pet pet, User user){
        return given().filters(new AuthFilter(user.getUsername(), user.getPassword()), new LogFilter())
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .contentType(ContentType.JSON)
                .body(pet)
                .post(EnvReader.getApiPets());
    }

    public Response getPet(Integer petId, User user){
        return given().filters(new AuthFilter(user.getUsername(), user.getPassword()), new LogFilter())
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .pathParam("petId", petId)
                .get(EnvReader.getApiPets()+"/{petId}");

    }

}
