package com.endava.petclinic;

import com.endava.petclinic.models.*;
import com.endava.petclinic.util.EnvReader;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import javax.xml.stream.events.EndDocument;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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

        //visit.setId(idVisit);

        assertThat(actualVisit, is(visit));

    }

    @Test
    public void putOwner(){
        Owner owner = new Owner();
        owner.setFirstName(faker.name().firstName());
        owner.setLastName(faker.name().lastName());
        owner.setCity(faker.address().city());
        owner.setAddress(faker.address().streetAddress());
        owner.setTelephone(faker.number().digits(10));

        ValidatableResponse ownerResponse = given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .contentType(ContentType.JSON)
                .body(owner)
                .log().all()
                .post(EnvReader.getApiOwners())
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        owner.setId(ownerResponse.extract().jsonPath().getInt("id"));

        owner.setFirstName(faker.name().firstName());
        owner.setCity(faker.address().city());

        given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .contentType(ContentType.JSON)
                .body(owner)
                .pathParam("ownerId", owner.getId())
                .log().all()
                .put(EnvReader.getApiOwners()+"/{ownerId}")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_NO_CONTENT);


        ValidatableResponse getResponse = given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .pathParam("ownerId", owner.getId())
                .log().all()
                .get(EnvReader.getApiOwners()+"/{ownerId}")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        Owner actualOwner = getResponse.extract().as(Owner.class);

        assertThat(actualOwner,is(owner));

    }

    @Test
    public void postPet(){
        //creezi un owner, creezi un owner, legi ownerul de pet
        //la fel si la pet type
        //add owner
        Owner owner= new Owner();
        owner.setFirstName(faker.name().firstName());
        owner.setLastName(faker.name().lastName());
        owner.setCity(faker.address().city());
        owner.setAddress(faker.address().streetAddress());
        owner.setTelephone(faker.number().digits(10));

        ValidatableResponse postOwner = given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .contentType(ContentType.JSON)
                .body(owner)
                .log().all()
                .post(EnvReader.getApiOwners())
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        owner.setId(postOwner.extract().jsonPath().getInt("id"));

        Type typePet = new Type(faker.animal().name());

        ValidatableResponse postType = given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .contentType(ContentType.JSON)
                .body(typePet)
                .log().all()
                .post(EnvReader.getApiPetTypes())
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        typePet.setId(postType.extract().jsonPath().getInt("id"));

        Pet pet = new Pet();
        pet.setOwner(owner);
        pet.setType(typePet);
        pet.setName(faker.name().firstName());
        //metoda pt birthDate fara generare manuala
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        pet.setBirthDate(formatter.format(faker.date().birthday(0,10)));

        ValidatableResponse postPet = given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .contentType(ContentType.JSON)
                .body(pet)
                .log().all()
                .post(EnvReader.getApiPets())
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

       // pet.setId(postOwner.extract().jsonPath().getInt("id"));

        Pet actualPet = postPet.extract().as(Pet.class);

        assertThat(actualPet, is(pet));

        //get owner to see that pet was added
        given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .pathParam("ownerId", owner.getId())
                .get(EnvReader.getApiOwners()+"/{ownerId}")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);


    }

    @Test
    public void postVisitTest(){

        Owner owner= new Owner();
        owner.setFirstName(faker.name().firstName());
        owner.setLastName(faker.name().lastName());
        owner.setCity(faker.address().city());
        owner.setAddress(faker.address().streetAddress());
        owner.setTelephone(faker.number().digits(10));

        ValidatableResponse postOwner = given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .contentType(ContentType.JSON)
                .body(owner)
                .log().all()
                .post(EnvReader.getApiOwners())
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        owner.setId(postOwner.extract().jsonPath().getInt("id"));

        Type typePet = new Type(faker.animal().name());

        ValidatableResponse postType = given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .contentType(ContentType.JSON)
                .body(typePet)
                .log().all()
                .post(EnvReader.getApiPetTypes())
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        typePet.setId(postType.extract().jsonPath().getInt("id"));

        Pet pet = new Pet();
        pet.setOwner(owner);
        pet.setType(typePet);
        pet.setName(faker.name().firstName());
        //metoda pt birthDate fara generare manuala
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        pet.setBirthDate(formatter.format(faker.date().birthday(0,10)));

        ValidatableResponse postPet = given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .contentType(ContentType.JSON)
                .body(pet)
                .log().all()
                .post(EnvReader.getApiPets())
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        pet.setId(postPet.extract().jsonPath().getInt("id"));

        //Integer idPet = postPet.extract().jsonPath().getInt("id");

        //returneaza o data in trecut cel mult 10 zile
        String dateV = formatter.format(faker.date().past(10, TimeUnit.DAYS));
        Visit visit = new Visit();
        visit.setDate(dateV);
        visit.setDescription(faker.medical().symptoms());
        visit.setPet(pet);

        ValidatableResponse postVisit = given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getbasePath())
                .contentType(ContentType.JSON)
                .body(visit)
                .log().all()
                .post(EnvReader.getApiVisits())
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        Visit actualVisit = postVisit.extract().as(Visit.class);

        assertThat(actualVisit, is(visit));





    }

    @Test
    public void testSecured(){
        Owner owner = new Owner();
        owner.setAddress(faker.address().streetAddress());
        owner.setCity(faker.address().city());
        owner.setFirstName(faker.name().firstName());
        owner.setLastName(faker.name().lastName());
        owner.setTelephone(faker.number().digits(10));


        ValidatableResponse response = given()
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePathSecured())
                .auth().preemptive().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .body(owner)
                .log().all()
                .post(EnvReader.getApiOwners())
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        Integer id = response.extract().jsonPath().getInt("id");

        ValidatableResponse getResponse = given()
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePathSecured())
                .auth().preemptive().basic("admin", "admin")
                .pathParam("ownerId", id)
                .log().all()
                .get(EnvReader.getApiOwners()+"/{ownerId}")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        Owner actualOwner = getResponse.extract().as(Owner.class);

        assertThat(actualOwner, is(owner));

    }

    @Test
    public void testUser(){
        //create new user
        User user = new User(faker.internet().password(),faker.name().username(), "OWNER_ADMIN");

        given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePathSecured())
                .auth().preemptive().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .body(user)
                .log().all()
                .post(EnvReader.getApiUsers())
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        //create new owner
        Owner owner = new Owner();
        owner.setAddress(faker.address().streetAddress());
        owner.setCity(faker.address().city());
        owner.setFirstName(faker.name().firstName());
        owner.setLastName(faker.name().lastName());
        owner.setTelephone(faker.number().digits(10));


        ValidatableResponse response = given()
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePathSecured())
                .auth().preemptive().basic(user.getUsername(), user.getPassword())
                .contentType(ContentType.JSON)
                .body(owner)
                .log().all()
                .post(EnvReader.getApiOwners())
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        Integer id = response.extract().jsonPath().getInt("id");

        ValidatableResponse getResponse = given()
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePathSecured())
                .auth().preemptive().basic(user.getUsername(), user.getPassword())
                .pathParam("ownerId", id)
                .log().all()
                .get(EnvReader.getApiOwners()+"/{ownerId}")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        Owner actualOwner = getResponse.extract().as(Owner.class);

        assertThat(actualOwner, is(owner));
    }

}
