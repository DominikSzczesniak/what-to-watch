package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.MovieDoestNotBelongToUserException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.UserDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MoviesToWatchService {

	private final MoviesRepository repository;
	private final UserProvider userProvider;

	public MovieId addMovieToList(final String movieTitle, final UserId userId) {
		if (!userProvider.exists(userId)) {
			throw new UserDoesNotExistException("User doesn't exist. Didn't add movie to any list");
		}
		final Movie movie = new Movie(repository.nextMovieId(), movieTitle, userId);
		repository.save(movie);
		return movie.getMovieId();
	}

	public void removeMovieFromList(final MovieId movieId, final UserId userId) {
		List<Movie> movies = getList(userId);
		if (movies.stream().noneMatch(movie -> movie.getMovieId().equals(movieId) && movie.getUserId().equals(userId))) {
			System.out.println("Didn't remove movie.");
		} else {
			repository.removeMovie(movieId, userId);
		} // TODO: przeniesc do infile
	}

	public List<Movie> getList(final UserId userId) {
		return repository.findAll(userId);
	}

}


