package com.endava.petclinic.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.javafaker.Bool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Boolean enabled;
    private String password;
    private String username;
    private List<Role> roles;  //OWNER_ADMIN/ VET_ADMIN

    public User(String password, String username, RoleName... roles) {
        this.enabled = true;
        this.password = password;
        this.username = username;
        this.roles = new ArrayList<>();
        for(RoleName roleName : roles){
            this.roles.add(new Role(roleName.getName()));
        }
    }

    public User() {
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "enabled=" + enabled +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(enabled, user.enabled) &&
                Objects.equals(password, user.password) &&
                Objects.equals(username, user.username) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, password, username, roles);
    }
}
