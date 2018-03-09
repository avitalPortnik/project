package com.avitalnurit.imdb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by avital on 16/02/2018.
 */
@RestController
public class StatController {

    @Autowired //tells spring to put the instance of MovieService in movieService
            MovieService movieService; //not to do new MovieService() because this will be an instance that is not managed by Spring

    @CrossOrigin
    @ResponseBody
    @RequestMapping("/{genre}/{role}/{decade}")
    public List<Integer> getGenderStatistics(@PathVariable("genre") String genre,
                                             @PathVariable("role") String role,
                                             @PathVariable("decade") String decade) {
        System.out.println("got this: " + genre);
        System.out.println("got this: " + role);
        System.out.println("got this: " + decade);
        //List<Integer> data = Arrays.asList(rand(), rand(), rand(), rand(), rand(), rand(), rand(), rand(), rand(), rand());
        List<Integer> data = movieService.processData(genre,role,decade); //example: decade 90, size of 10. data[i] = percentage of men in year 9i
        System.out.println(data);
        return data;
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping("/{genre}/{decade}")
    public List<Map<String, String>> getMoviesByDecade(@PathVariable("genre") String genre,
                                                       @PathVariable("decade") String decade) {
        System.out.println("got this: " + decade);
        List<Map<String, String>> data = movieService.processMoviesByDecade(genre, decade);
        System.out.println(data);
        Collections.sort(data, Comparator.comparingInt(m -> Integer.parseInt(m.get("date").substring(0, 4))));
        return data;
    }

}
