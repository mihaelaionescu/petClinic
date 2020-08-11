package com.endava.petclinic.data;

import com.endava.petclinic.models.*;
import com.github.javafaker.Faker;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class DataGenerator {

    private Faker faker = new Faker();

    public User getUser(RoleName... roles) {
        return new User(faker.internet().password(), faker.name().username(), roles);
    }

    public Owner getOwner() {
        Owner owner = new Owner();
        owner.setAddress(faker.address().streetAddress());
        owner.setCity(faker.address().city());
        owner.setFirstName(faker.name().firstName());
        owner.setLastName(faker.name().lastName());
        owner.setTelephone(faker.number().digits(10));

        return owner;
    }

    public Type getType(){
        Type type = new Type();
        type.setName(faker.animal().name());

        return type;
    }

    public Pet getPet(Owner owner, Type typePet){
        Pet pet = new Pet();
        pet.setOwner(owner);
        pet.setType(typePet);
        pet.setName(faker.name().firstName());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        pet.setBirthDate(formatter.format(faker.date().birthday(0,10)));

        return pet;
    }

    public Visit getVisit(Pet pet){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dateV = formatter.format(faker.date().past(10, TimeUnit.DAYS));
        Visit visit = new Visit();
        visit.setDate(dateV);
        visit.setDescription(faker.medical().symptoms());
        visit.setPet(pet);

        return visit;
    }
}
