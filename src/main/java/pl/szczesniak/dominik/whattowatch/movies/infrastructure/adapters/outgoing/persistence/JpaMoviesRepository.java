package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.Movie;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesRepository;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagLabel;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaMoviesRepository implements MoviesRepository {

	private final SpringDataJpaMoviesRepository springDataJpaMoviesRepository;

	@Override
	public void create(final Movie movie) {
		springDataJpaMoviesRepository.save(movie);
	}

	@Override
	public void update(final Movie movie) {
			springDataJpaMoviesRepository.save(movie);
	}

	@Override
	public List<Movie> findAll(final UserId userId) {
		return springDataJpaMoviesRepository.findAllByUserId(userId);
	}

	@Override
	public void removeMovie(final MovieId movieId, final UserId userId) {
		springDataJpaMoviesRepository.deleteById(movieId.getValue());
	}

	@Override
	public Optional<Movie> findBy(final MovieId movieId, final UserId userId) {
		return springDataJpaMoviesRepository.findByMovieIdAndUserId(movieId.getValue(), userId);
	}

	@Override
	public List<Movie> findAllByTagLabel(final TagLabel tagLabel, final UserId userId) {
		return null;
	}

}
