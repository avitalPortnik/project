package com.avitalnurit.imdb;
import java.util.ArrayList;

/**
 * Created by avital on 07/01/2018.
 */
public class Movie {
    private String title;
    private int id;
    private String date;
    private ArrayList<String> genres;
    private ArrayList<Person> actors;
    private ArrayList<Person> stuff;

    public Movie(String title, int id, String date,ArrayList<Person> actors, ArrayList<Person> stuff , ArrayList<String> genres) {
        this.title = title;
        this.id = id;
        this.date = date;
        this.actors = actors;
        this.stuff = stuff;
        this.genres=genres;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", date='" + date + '\'' +
                ", genres=" + genres +
                ", actors=" + actors +
                ", stuff=" + stuff +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public ArrayList<Person> getActors() {
        return actors;
    }

    public void setActors(ArrayList<Person> actors) {
        this.actors = actors;
    }

    public ArrayList<Person> getStuff() {
        return stuff;
    }

    public void setStuff(ArrayList<Person> stuff) {
        this.stuff = stuff;
    }

}
