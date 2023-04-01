package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserService;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MoviesToWatchService {

    private final MoviesRepository repository;
    private final UserService userService;

    public void addMovieToList(final String movieTitle, final UserId userId) {
        if (!userService.exists(userId)) {
            System.out.println("user does not exist");
            return;
        }
        Movie movie = new Movie(new MovieId(repository.nextMovieId()), movieTitle, userId);
        repository.save(movie);
    }

    public void removeMovieFromList(final String title, final UserId id) {
        getList(id).forEach(movie -> {
                    if (movie.getTitle().equals(title)) {
                        repository.removeMovie(movie.getMovieId());
                    }
                });
    }

    public List<Movie> getList(final UserId userId) {
        return repository.findAll(userId);
    }
}


