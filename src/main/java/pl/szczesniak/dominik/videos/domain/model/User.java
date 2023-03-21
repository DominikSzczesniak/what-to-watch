package pl.szczesniak.dominik.videos.domain.model;

import lombok.Getter;
import pl.szczesniak.dominik.videos.domain.model.movie.Movie;

import java.util.ArrayList;

public class User {

    @Getter
    private final UserID id;
    private final ArrayList<Movie> movieList = new ArrayList<>();

    public User() {
        id = new UserID();
    }

    public ArrayList<Movie> getUsersMovieList() {
        return movieList;
    }
}
