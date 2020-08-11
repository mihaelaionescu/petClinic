package com.endava.petclinic.models;

public enum RoleName {
    //reprezentarea enum-ului prin string se face prin nume
    OWNER_ADMIN("OWNER_ADMIN"), VET_ADMIN("VET_ADMIN");

    private String name;

    //la enum constructorul se foloseste doar in interior
    private RoleName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
