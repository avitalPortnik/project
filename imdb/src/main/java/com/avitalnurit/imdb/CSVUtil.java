package com.avitalnurit.imdb;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CSVUtil {

    private static final String MOVIES_CSV_FILE = "./public/movies.csv";
    private static final String ACTORS_CSV_FILE = "./public/actors.csv";
    private static final String STAFF_CSV_FILE = "./public/staff.csv";
    private static final String[] MOVIE_CSV_HEADER = {"title","date","actors","staff","genres"};
    private static final String[] PERSON_CSV_HEADER = {"name","gender"};
    private static final int MALE = 2;

    public void generateMoviesCSV(List<Movie> movies) {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(MOVIES_CSV_FILE));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader(MOVIE_CSV_HEADER))
        ) {
            for (Movie movie : movies) {
                csvPrinter.printRecord(toCSVMovieRow(movie));
            }
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateActorsCSV(List<Movie> movies) {
        //get all actors in one big list
        List<Person> allActors = movies.stream()
                .map(m -> m.getActors())
                .flatMap(List::stream)
                .collect(Collectors.toList());
        //remove duplicates
        Set<Person> allActorsSet = new HashSet<>(allActors);
        generatePersonCSV(allActorsSet, ACTORS_CSV_FILE);
    }

    public void generateStaffCSV(List<Movie> movies) {
        List<Person> allStaff = movies.stream()
                .map(m -> m.getStuff())
                .flatMap(List::stream)
                .collect(Collectors.toList());
        Set<Person> allStaffSet = new HashSet<>(allStaff);
        generatePersonCSV(allStaffSet, STAFF_CSV_FILE);
    }

    private void generatePersonCSV(Iterable<Person> people, String filePath) {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader(PERSON_CSV_HEADER))
        ) {
            for (Person p : people) {
                csvPrinter.printRecord(Arrays.asList(p.getName(), p.getGender() == MALE ? "m" : "f"));
            }
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> toCSVMovieRow(Movie movie) {
        return Arrays.asList(
                movie.getTitle(),
                movie.getDate(),
                movie.getActors().stream().map(x -> x.getName()).collect(Collectors.toList()).toString(),
                movie.getStuff().stream().map(x -> x.getName()).collect(Collectors.toList()).toString(),
                movie.getGenres().toString()
        );

    }
}
