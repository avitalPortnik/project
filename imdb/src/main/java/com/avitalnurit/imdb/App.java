package com.avitalnurit.imdb;


import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) throws UnirestException {
        SpringApplication.run(App.class, args);
    }
}
