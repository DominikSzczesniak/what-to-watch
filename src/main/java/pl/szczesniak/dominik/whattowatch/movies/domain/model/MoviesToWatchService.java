package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.User;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MoviesToWatchService {

    private final MoviesRepository repository;

    public void addMovieToList(final User user, final String movieTitle, final UserId userId) {
        Movie movie = new Movie(repository.nextMovieId(), movieTitle, userId);
        if (getList(user)
                .stream()
                .noneMatch(title -> movieTitle.equals(title.getTitle()))) {
            repository.save(movie);
        }
    }

    public void removeMovieFromList(final User user, final String title) {
        getList(user).forEach(movie -> {
                    if (movie.getTitle().equals(title)) {
                        repository.removeMovie(movie);
                    }
                });
    }


    public List<Movie> getList(final User user) {
        return repository.findAll(user.getId());
    }

    public List<String> getMovieTitles(final User user) {
        List<String> movieTitles = new ArrayList<>();
        repository.findAll(user.getId()).forEach(movie -> movieTitles.add(movie.getTitle()));
        return movieTitles;
    }
}


