package com.endava.petclinic.auth;

import com.endava.petclinic.models.User;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class AuthFilter implements Filter {

    private String username;
    private String password;

    public AuthFilter(String username, String password) {
        this.password = password;
        this.username = username;
    }

    @Override
    public Response filter(FilterableRequestSpecification filterableRequestSpecification,
                           FilterableResponseSpecification filterableResponseSpecification,
                           FilterContext filterContext) {


        filterableRequestSpecification.auth().preemptive().basic(this.username, this.password);

        Response response = filterContext.next(filterableRequestSpecification, filterableResponseSpecification);

        return response;
    }
}
