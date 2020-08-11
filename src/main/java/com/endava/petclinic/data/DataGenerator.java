package com.endava.petclinic.data;

import com.endava.petclinic.models.Owner;
import com.endava.petclinic.models.RoleName;
import com.endava.petclinic.models.User;
import com.github.javafaker.Faker;

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
}
