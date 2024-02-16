package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

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
	public void removeMovie(final MovieId movieId, final UserId userId) {
		springDataJpaMoviesRepository.deleteById(movieId.getValue());
	}

	@Override
	public Optional<Movie> findBy(final MovieId movieId, final UserId userId) {
		return springDataJpaMoviesRepository.findByIdAndUserId(movieId.getValue(), userId);
	}

}
