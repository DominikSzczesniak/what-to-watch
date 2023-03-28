package pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence;

import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.Movie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MoviesRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@ToString
public class InMemoryMoviesRepository implements MoviesRepository {

    public static final AtomicInteger nextId = new AtomicInteger();
    private final Map<MovieId, Movie> movies = new HashMap<>();

    @Override
    public int nextMovieId() {
        return nextId.incrementAndGet();
    }

    @Override
    public void save(final Movie movie) {
        movies.put(movie.getMovieId(), movie);
    }

    @Override
    public List<Movie> findAll(final UserId userId) {
        return movies.values().stream()
                .filter(movie -> movie.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void removeMovie(final MovieId movieId) {
        movies.remove(movieId);
    }

}
