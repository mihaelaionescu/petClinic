package com.endava.petclinic.loggig;


import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.internal.print.RequestPrinter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.util.HashSet;

public class LogFilter implements Filter{

    @Override
    public Response filter(FilterableRequestSpecification filterableRequestSpecification,
                           FilterableResponseSpecification filterableResponseSpecification,
                           FilterContext filterContext) {
        //trebuie sa trimitem requestul mai departe
        //next intoarce un response
        //pt ca noi vrem log-uri pe request
        //filterableRequestSpecification.log().all();

        RequestPrinter.print(filterableRequestSpecification,
                filterableRequestSpecification.getMethod(),
                filterableRequestSpecification.getURI(),
                LogDetail.ALL, new HashSet<>(),
                System.out, true);

        Response response = filterContext.next(filterableRequestSpecification, filterableResponseSpecification);

        //logare automata in consola
        response.prettyPeek();

        return response;
    }
}
