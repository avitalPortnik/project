package com.avitalnurit.imdb;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by avital on 16/12/2017.
 */
@Component //this says basically the class is singelton if used in spring context
public class MovieService {

    public static final int PAGES =406;
    public static final int FEMALE = 1;
    public static final String ACTOR = "actor";

    OkHttpClient client = new OkHttpClient();
    private ArrayList<Movie> movies = new ArrayList<>();
    public Map<String, ArrayList<Movie>> moviesByDecade = new HashMap<>();

    @Autowired
    private CSVUtil csvUtil;


    @PostConstruct //after construction of instance of class run this method
    public void init() throws InterruptedException {
        System.out.println("init movie service");
        ArrayList<Integer> movieIds = new ArrayList<>();
        getMoviesIds(movieIds);

        for (int i = 0; i < movieIds.size(); i++) {
            if (i % 20 == 0 && i > 0) {
                Thread.sleep(10000);
                System.out.println(".");
            }
            getMovie(movieIds.get(i));
        }

        initMapOfDecades();
        csvUtil.generateActorsCSV(movies);
        csvUtil.generateStaffCSV(movies);
        csvUtil.generateMoviesCSV(movies);
    }


    public void getMoviesIdFromPage(int page, ArrayList arr) throws UnirestException {
        HttpResponse<JsonNode> movieList;
        movieList = Unirest.get("https://api.themoviedb.org/3/movie/top_rated?api_key=0b35d0ccdd235fcce658c9d53f526dbb&language=en-US&page=" + page)
                .header("accept", "application/json")
                .header("cache-control", "no-cache")
                .asJson();
        JSONObject movies = movieList.getBody().getObject();
        JSONArray moviesArray;
        if (!movies.has("results")) {
            return;
        }
        moviesArray = movies.getJSONArray("results");
        for (int i = 0; i < moviesArray.length(); i++) {
            int id = moviesArray.getJSONObject(i).getInt("id");
            arr.add(id);
        }
    }

    public ArrayList getMoviesIds(ArrayList arr) throws InterruptedException {
        for (int i = 1; i <= PAGES; i++) {
            if (i % 35 == 0 && i > 0) {
                Thread.sleep(10000);
                System.out.println(".");
            }
            try {
                getMoviesIdFromPage(i, arr);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    public List<Movie> getMoviesByGenre(List<Movie> movies, String genre) {
        List<Movie> genres = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getGenres().contains(genre)) {
                genres.add(movies.get(i));
            }
        }
        return genres;
    }


    public void initMapOfDecades() {
        for (int i = 0; i < movies.size(); i++) {
            String year;
            if(movies.get(i).getDate().length()!=0){
             year = movies.get(i).getDate().substring(0, 4);}
             else {
                year="1930";
            }
            int yearInt = (Integer.parseInt(year) / 10) * 10;
            String decade = String.valueOf(yearInt).substring(2, 4);
            if (!moviesByDecade.containsKey(decade)) {
                moviesByDecade.put(decade, new ArrayList<>());
            }
            moviesByDecade.get(decade).add(movies.get(i));
        }
    }

    public void countByDecade() {
        Map<String, Integer> countByDecade = new HashMap<>();
        for (int i = 0; i < movies.size(); i++) {
            String decade = getDecadeFromYear(i);
            countByDecade.put(decade, countByDecade.getOrDefault(decade, 0) + 1);
        }
    }

    private String getDecadeFromYear(int i) {
        String year = movies.get(i).getDate().substring(0, 4);
        int yearInt = (Integer.parseInt(year) / 10) * 10;
        return String.valueOf(yearInt).substring(2, 4);
    }



     /*try {
            infoJson = Unirest.get("https://api.themoviedb.org/3/movie/" + id + "?api_key=0b35d0ccdd235fcce658c9d53f526dbb&language=en-US")
                    .header("content-type", "application/json")
                    .header("cache-control", "no-cache")
                    .asJson().getBody().getObject();



            Thread.sleep(100);

            movieCredits = Unirest.get("https://api.themoviedb.org/3/movie/" + id + "/credits?api_key=0b35d0ccdd235fcce658c9d53f526dbb")
                    .header("content-type", "application/json")
                    .header("cache-control", "no-cache")
                    .asJson().getBody().getObject();
        } catch (UnirestException e) {
            e.printStackTrace();
            return;
        }*/

    public void getMovie(int id) throws InterruptedException {
        JSONObject infoJson;
        JSONObject movieCredits;
        String jsonDataInfo = null;
        String jsonDataCredit = null;
        JSONObject JobjectInfo = null;
        JSONObject JobjectCredit = null;


        try {
            Request requestInfo = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/" + id + "?api_key=0b35d0ccdd235fcce658c9d53f526dbb&language=en-US")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "bbfc13e7-26b7-0ee4-b0e7-b767fab76810")
                    .build();

            Thread.sleep(100);

            Request requestCredit = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/" + id + "/credits?api_key=0b35d0ccdd235fcce658c9d53f526dbb")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "bbfc13e7-26b7-0ee4-b0e7-b767fab76810")
                    .build();

            Response responseInfo = null;
            Response responseCredit = null;
            try {
                responseInfo = client.newCall(requestInfo).execute();
                responseCredit = client.newCall(requestCredit).execute();
                jsonDataInfo = responseInfo.body().string();
                jsonDataCredit = responseCredit.body().string();
                JobjectInfo = new JSONObject(jsonDataInfo);
                JobjectCredit = new JSONObject(jsonDataCredit);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("BBBBBBBBBBBBB!!!!!!!BBBBBBBBBB");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("AAAAAAAAAAAAA!!!!!!!AAAAAAAAAAAAAA");
            return;
        }


        if (!JobjectInfo.has("genres")
                || !JobjectInfo.has("title")
                || !JobjectInfo.has("release_date")
                || !JobjectCredit.has("cast")
                || !JobjectCredit.has("crew")) {
            return;
        }
        JSONArray genresJson = JobjectInfo.getJSONArray("genres");
        JSONArray castJson = JobjectCredit.getJSONArray("cast");
        JSONArray staffJson = JobjectCredit.getJSONArray("crew");

        String title = JobjectInfo.getString("title");
        String date = JobjectInfo.getString("release_date");
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Person> actors = new ArrayList<>();
        ArrayList<Person> staff = new ArrayList<>();

        for (int i = 0; i < Math.min(castJson.length(), 20); i++) {
            String name = castJson.getJSONObject(i).getString("name");
            int gender = castJson.getJSONObject(i).getInt("gender");
            //todo: ignor '0' gender
            if (gender != 0) {
                actors.add(new Person(name, gender, "actor"));
            }
        }


        for (int i = 0; i < genresJson.length(); i++) {
            genres.add(genresJson.getJSONObject(i).getString("name"));
        }


        //1 is female and 2 is male
        for (int i = 0; i < Math.min(staffJson.length(), 20); i++) {
            String name = staffJson.getJSONObject(i).getString("name");
            String job = staffJson.getJSONObject(i).getString("job");
            int gender = staffJson.getJSONObject(i).getInt("gender");
            if (gender != 0) { //gender is known
                staff.add(new Person(name, gender, job));
            }
        }

        movies.add(new Movie(title, id, date, actors, staff, genres));
    }

//    public List<Integer> processData(String genre, String role, String decade) {
//        List<Integer> ret = Arrays.asList(10, 10, 10, 10, 10, 10, 10, 10, 10, 10);
//        ArrayList<Movie> moviesByGenre = getMoviesByGenre(movies, genre);
//
//        int decadeInt = Integer.parseInt(decade);
//        int counterMen = 0;
//        int counterWomen = 0;
//
//
//        for (int k = decadeInt; k < decadeInt + 10; k++) { // ["00":2000,"60":1960]
//            for (int i = 0; i < moviesByGenre.size(); i++) {
//                Movie m = moviesByGenre.get(i);
//                String date = m.getDate();
//                String year = date.substring(2, 4); //68
//                ArrayList<Person> people;
//                int yearInt = Integer.parseInt(year);
//
//                if (yearInt == k) {
//                    if (role.equals("actor"))
//                        people = m.getActors();
//                    else
//                        people = m.getStuff();
//                    for (int j = 0; j < people.size(); j++) {
//                        int gen = people.get(j).getGender();
//                        if (gen == 1)
//                            counterWomen++;
//                        else if (gen == 2)
//                            counterMen++;
//                    }
//                }
//                int l = counterMen;
//
//            }
//            if ((counterMen + counterWomen) != 0) {
//                double allCounter = counterMen + counterWomen;
//                double hh = (counterMen / allCounter) * 100;
//                ret.set(k - decadeInt, (int) hh);
//            } else
//                ret.set(k - decadeInt, 60);
//            counterMen = 0;
//            counterWomen = 0;
//        }
//        return ret;
//    }


    public Map<Integer, List<Movie>> moviesByYear(List<Movie> movies) {
        Map<Integer, List<Movie>> mapByYears = new HashMap<>();
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            Integer year = Integer.valueOf(movie.getDate().substring(2, 4));
            if (!mapByYears.containsKey(year)) {
                mapByYears.put(year, new ArrayList<>());
            }
            mapByYears.get(year).add(movie);
        }
        return mapByYears;
    }

    public List<Integer> processData(String genre, String role, String decade) {
        List<Movie> moviesFromDecade = getMoviesByGenre(moviesByDecade.getOrDefault(decade,new ArrayList<>()), genre);
        double menCounter = 0;
        double womenCounter = 0;
        Map<Integer, List<Movie>> mapByYears = moviesByYear(moviesFromDecade);
        List<Person> people;
        Integer decadeInteger = Integer.valueOf(decade);
        List<Integer> res = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        for (int i = 0; i <= 9; i++) {
            if (mapByYears.containsKey(decadeInteger + i)) {
                List<Movie> moviesList = mapByYears.get(decadeInteger + i);
                for (int m = 0; m < moviesList.size(); m++) {
                    Movie movie = moviesList.get(m);
                    if (role.equals(ACTOR)) {
                        people = movie.getActors();
                    } else {
                        people = movie.getStuff();
                    }
                    for (int k = 0; k < Math.min(10, people.size()); k++) {
                        if (people.get(k).getGender() == FEMALE) {
                            womenCounter++;
                        } else {
                            menCounter++;
                        }
                    }
                }
                double total = womenCounter + menCounter;
                double result = (menCounter /total) *100;
                res.set(i, (int) result);
                womenCounter = 0;
                menCounter = 0;
            } else {
                res.set(i, -1);
            }
        }
        return res;
    }


    public List<Map<String, String>> processMoviesByDecade(String genre, String decade) {
        List<Map<String, String>> res = new ArrayList<>();
        List<Movie> movies = moviesByDecade.getOrDefault(decade, new ArrayList<>());
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getGenres().contains(genre)) {
                Map<String, String> movieObject = new HashMap<>();
                movieObject.put("name", movies.get(i).getTitle());
                movieObject.put("date", movies.get(i).getDate());
                movieObject.put("id", String.valueOf(movies.get(i).getId()));
                res.add(movieObject);
            }
        }
        return res;
    }

}
