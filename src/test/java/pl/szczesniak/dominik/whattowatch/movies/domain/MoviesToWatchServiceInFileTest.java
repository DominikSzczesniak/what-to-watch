package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.MovieDoestNotBelongToUserException;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence.InFileMoviesRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class MoviesToWatchServiceInFileTest { // FIXME : ZLE!!!

	String filepath = "src/test/resources/moviesTest.csv";
	private InMemoryUserProvider userProvider;
	private MoviesToWatchService tut;

	@BeforeEach
	void setUp() {
		tut = new MoviesToWatchService(new InFileMoviesRepository(filepath), userProvider);
	}

	@Test
	void should_throw_exception_and_not_remove_movie_when_movieid_doesnt_belong_to_userid() {
		// when
		Throwable thrown = catchThrowable(() -> tut.removeMovieFromList(new MovieId(1), new UserId(2)));

		// then
		assertThat(thrown).isInstanceOf(MovieDoestNotBelongToUserException.class);
		assertThat(tut.getList(new UserId(1))).extracting(Movie::getMovieId).contains(new MovieId(1));
	}
}