package com.endava.petclinic;

import com.endava.petclinic.models.Owner;
import com.endava.petclinic.models.Pet;
import com.endava.petclinic.models.Type;
import com.endava.petclinic.models.Visit;
import com.endava.petclinic.util.EnvReader;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PetClinicTest {

    //faker functioneaza pe baza de obiect
    //limba default e engleza, schimbi cu Locale.Limba
    private Faker faker = new Faker();

    @Test
    public void firstTest(){

        Owner owner = new Owner();
        owner.setAddress(faker.address().streetAddress());
        owner.setCity(faker.address().city());
        owner.setFirstName(faker.name().firstName());
        owner.setLastName(faker.name().lastName());
        owner.setTelephone(faker.number().digits(10));
        //nu merge cu faker.phone pt ca pune si +..


        ValidatableResponse response = given()
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .contentType(ContentType.JSON)  //adauga un header cu valoarea data de noi
                .body(owner) //aici restul in face serializare
                .log().all()
                .post(EnvReader.getApiOwners())
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        Integer id = response.extract().jsonPath().getInt("id");

//        given()//request
//                .baseUri("http://bhdtest.endava.com")
//                .port(8080)
//                .basePath("petclinic")
//                .pathParam("ownerId", id)
//                .log().all()
//                .get("/api/owners/{ownerId}")
//                //response
//                .prettyPeek()
//                .then().statusCode(HttpStatus.SC_OK)
//                    .body("id", is(1) )
//                    .body("firstName", is("George"))
//                    .body("lastName", startsWith("Frank"))
//                    .body("address", containsString("W. Liberty"))
//                    .body("city", equalToIgnoringCase("madison"))
//                    .body("telephone",hasLength(10)

        ValidatableResponse getResponse = given()//request
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .pathParam("ownerId", id)
                .log().all()
                .get(EnvReader.getApiOwners()+"/{ownerId}")
                //response
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        Owner actualOwner = getResponse.extract().as(Owner.class);  //extrage body-ul responsului intr-un obiect de tip owner

        //validare de HamCrest
        //actual result, exprected result = matcher
        //cand folosim asta, nu mai e nevoie de toate asserturile de la .body()
        assertThat(actualOwner, is(owner));

    }

    public String randomDate(){
        int day, month, year;
        year = new Random().ints(2000,2020).findFirst().getAsInt();
        month = new Random().ints(1,12).findFirst().getAsInt();
        if ((month<=7 && month%2==1) || (month>=8 && month%2==1)) {
            day = new Random().ints(1,31).findFirst().getAsInt();
        }
        else {
            if (month==2){
                if (year%4==0) {
                    day = new Random().ints(1,29).findFirst().getAsInt();
                }
                else{
                    day = new Random().ints(1,28).findFirst().getAsInt();
                }
            }
            else{
                day = new Random().ints(1,30).findFirst().getAsInt();
            }
        }
        String date = year+"/"+month+"/"+day;
        return date;
    }

    @Test
    public void addPet(){
        //get an owner for test
        ValidatableResponse getResponseO = given()
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .pathParam("ownerId", 18)
                //.log().all()
                .get(EnvReader.getApiOwners()+"/{ownerId}")
                //.prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        Owner actualOwner = getResponseO.extract().as(Owner.class);

        //get a type for test
        ValidatableResponse getResponseT = given()
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .pathParam("petTypeId", 1)
                //.log().all()
                .get(EnvReader.getApiPetTypes()+"/{petTypeId}")
                //.prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        Type type = getResponseT.extract().as(Type.class);

        Pet pet = new Pet();

        pet.setName(faker.name().firstName());
        pet.setBirthDate(randomDate());

        pet.setOwner(actualOwner);
        pet.setType(type);

        //post the created pet
        ValidatableResponse responseAddPet = given()
                                                    .baseUri(EnvReader.getBaseUri())
                                                    .port(EnvReader.getPort())
                                                    .basePath(EnvReader.getbasePath())
                                                    .contentType(ContentType.JSON)
                                                    .body(pet)
                                                    //.log().all()
                                                    .post(EnvReader.getApiPets())
                                                    //.prettyPeek()
                                                    .then().statusCode(HttpStatus.SC_CREATED);

        Integer idPet = responseAddPet.extract().jsonPath().getInt("id");

        //get the created pet
        ValidatableResponse responseCheckAdd = given().baseUri(EnvReader.getBaseUri())
                    .port(EnvReader.getPort())
                    .basePath(EnvReader.getbasePath())
                    .pathParam("petId", idPet)
                    .log().all()
                    .get(EnvReader.getApiPets()+"/{petId}")
                    .prettyPeek()
                    .then().statusCode(HttpStatus.SC_OK);

        Pet postedPet = responseCheckAdd.extract().as(Pet.class);

        assertThat(postedPet, is(pet));
    }

    @Test
    public void getPetList(){
        given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .log().all()
                .get(EnvReader.getApiPets())
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void postVisit(){
        Visit visit = new Visit();
        visit.setDate(randomDate());
        visit.setDescription(faker.medical().symptoms());

        //get a pet
        ValidatableResponse responsePet = given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .pathParam("petId", 4)
                //.log().all()
                .get(EnvReader.getApiPets()+"/{petId}")
                //.prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);
        Pet pet = responsePet.extract().as(Pet.class);

        visit.setPet(pet);

        ValidatableResponse responseAddVisit = given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .contentType(ContentType.JSON)
                .body(visit)
                .log().all()
                .post(EnvReader.getApiVisits())
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        Integer idVisit = responseAddVisit.extract().jsonPath().getInt("id");

        ValidatableResponse responseCheckVisit = given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .pathParam("visitId", idVisit)
                .log().all()
                .get(EnvReader.getApiVisits()+"/{visitId}")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        Visit actualVisit = responseCheckVisit.extract().as(Visit.class);

        assertThat(actualVisit, is(visit));

    }
}
