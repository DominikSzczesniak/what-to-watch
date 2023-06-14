package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.Movie;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesRepository;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@RequiredArgsConstructor
public class JpaMoviesRepository implements MoviesRepository {

	private final SpringDataJpaMoviesRepository springDataJpaMoviesRepository;

	private final AtomicInteger nextId = new AtomicInteger(0);

	@Override
	public MovieId nextMovieId() {
		return new MovieId(nextId.incrementAndGet());
	}

	@Override
	public void create(final Movie movie) {
		springDataJpaMoviesRepository.save(movie);
	}

	@Override
	public void update(final Movie movie) {
		springDataJpaMoviesRepository.saveAndFlush(movie);
	}

	@Override
	public List<Movie> findAll(final UserId userId) {
		return springDataJpaMoviesRepository.findAllByUserId(userId);
	}

	@Override
	public void removeMovie(final MovieId movieId, final UserId userId) {
		springDataJpaMoviesRepository.deleteByMovieIdAndUserId(movieId, userId);
	}

	@Override
	public Optional<Movie> findBy(final MovieId movieId, final UserId userId) {
		return Optional.empty();
	}

}
