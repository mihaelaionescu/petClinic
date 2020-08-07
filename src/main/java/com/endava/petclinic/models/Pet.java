package com.endava.petclinic.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pet {
    private String birthDate;
    private Integer id;
    private String name;
    private Owner owner;
    private Type type;

    public Pet(String birthDate, String name) {
        this.birthDate = birthDate;
        this.name = name;
    }

    public Pet() {
    }

    public Pet(String birthDate, String name, Owner owner, Type type) {
        this.birthDate = birthDate;
        this.name = name;
        this.owner = owner;
        this.type = type;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "birthDate='" + birthDate + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return Objects.equals(birthDate, pet.birthDate) &&
                Objects.equals(name, pet.name) &&
                Objects.equals(owner, pet.owner) &&
                Objects.equals(type, pet.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(birthDate, name, owner, type);
    }
}
