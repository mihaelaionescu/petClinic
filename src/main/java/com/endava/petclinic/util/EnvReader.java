package com.endava.petclinic.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvReader {

    private static Properties properties = new Properties();

    //bloc static
    //variab e statica pt ca o folosim in bloc static
    static {
        //se ruleaza automat cand se incarca totul in memorie
        //se executa o singura data pe durata rularii unui cod
        //incarcam datele din fisier trebuie incarcate

        InputStream is = EnvReader.class.getClassLoader().getResourceAsStream("env.properties");

        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getBaseUri(){
        return properties.getProperty("baseUri");
    }

    public static Integer getPort(){
        return Integer.parseInt(properties.getProperty("port"));
    }

    public static String getbasePath(){
        return properties.getProperty("basePath");
    }

    public static String getApiOwners(){
        return properties.getProperty("apiOwners");
    }

    public static String getApiPetTypes(){
        return properties.getProperty("apiPetTypes");
    }

    public static String getApiPets(){
        return properties.getProperty("apiPets");
    }

    public static String getApiVisits(){
        return  properties.getProperty("apiVisits");
    }

    public static String getBasePathSecured(){
        return properties.getProperty("basePathSecured");
    }
    public static String getApiUsers(){
        return properties.getProperty("apiUsers");
    }

}
