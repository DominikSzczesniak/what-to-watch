package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.UserDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MoviesToWatchService {

    private final MoviesRepository repository;
    private final UserProvider userProvider;

    public void addMovieToList(final String movieTitle, final UserId userId) {
        if (!userProvider.exists(userId)) {
            throw new UserDoesNotExistException("User doesn't exist. Didn't add movie to any list");
        }
        final Movie movie = new Movie(repository.nextMovieId(), movieTitle, userId);
        repository.save(movie);
    }

    public void removeMovieFromList(final String title, final UserId id) {
        final List<Movie> result = getList(id).stream()
                .filter(movie -> movie.getTitle().equals(title)).toList();
        repository.removeMovie(result.get(0).getMovieId());
    }

    public List<Movie> getList(final UserId userId) {
        return repository.findAll(userId);
    }
}


