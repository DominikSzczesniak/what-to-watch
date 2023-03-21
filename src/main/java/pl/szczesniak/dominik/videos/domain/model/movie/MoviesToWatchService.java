package pl.szczesniak.dominik.videos.domain.model.movie;

import pl.szczesniak.dominik.videos.domain.exceptions.MovieIsNotOnTheListException;
import pl.szczesniak.dominik.videos.domain.model.User;

import java.util.ArrayList;

public class MoviesToWatchService {



    public void addMovieToList(User user, Movie movie) {
        if (!getList(user).contains(movie)) {
            getList(user).add(movie);
        }
    }

    public void removeMovieFromList(User user, Movie movie) {
        if (!getList(user).contains(movie)) {
            throw new MovieIsNotOnTheListException("Movie" + movie + "is not on the list");
        } else {
            getList(user).remove(movie);
        }
    }

    public ArrayList<Movie> getList(User user) {
        return user.getUsersMovieList();
    }
}
