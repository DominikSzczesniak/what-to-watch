package pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.szczesniak.dominik.whattowatch.movies.domain.Movie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InFileMoviesRepositoryIntTest {

	@TempDir
	private File testFileMovies = new File("intTemporaryFile.txt");
	@TempDir
	private File testFileId = new File("intTemporaryFileId.txt");

	private String existingMoviesDbFilepath;
	private String existingMoviesIdDbFilepath;
	private InFileMoviesRepository tut;

	@BeforeEach
	void setUp() {
		existingMoviesDbFilepath = "src/test/resources/movies.csv";
		existingMoviesIdDbFilepath = "src/test/resources/moviesId.csv";
		tut = new InFileMoviesRepository(testFileMovies.getAbsolutePath() + testFileMovies.getName(),
				testFileId.getAbsolutePath() + testFileId.getName());
	}

	@Test
	void should_find_all_users_movies_from_given_file() {
		// given
		tut = new InFileMoviesRepository(existingMoviesDbFilepath, existingMoviesIdDbFilepath);

		// when
		List<Movie> movies = tut.findAll(new UserId(1));

		// then
		assertThat(movies.size()).isEqualTo(3);
	}

	@Test
	void should_return_8_when_asked_for_next_movie_id_from_given_file() {
		// when
		tut = new InFileMoviesRepository(existingMoviesDbFilepath, existingMoviesIdDbFilepath);

		// then
		assertThat(tut.findNextMovieId()).isEqualTo(new MovieId(8));
	}

	@Test
	void user_should_save_movie() {
		// given
		final UserId userId = new UserId(1);

		// when
		tut.save(new Movie(tut.nextMovieId(), new MovieTitle("Parasite"), userId));
		tut.save(new Movie(tut.nextMovieId(), new MovieTitle("Hulk"), userId));

		// then
		assertThat(tut.findAll(userId)).hasSize(2);
	}

	@Test
	void user_should_delete_movie() {
		// given
		final UserId userId = new UserId(1);
		tut.save(new Movie(tut.nextMovieId(), new MovieTitle("Parasite"), userId));
		tut.save(new Movie(tut.nextMovieId(), new MovieTitle("Hulk"), userId));

		// when
		tut.removeMovie(new MovieId(1), userId);

		// then
		assertThat(tut.findAll(userId)).hasSize(1);
	}

	@Test
	void should_not_remove_movie_when_movieid_doesnt_belong_to_userid() {
		// given
		final UserId userIdOne = new UserId(1);
		final UserId userIdTwo = new UserId(2);

		tut.save(new Movie(tut.nextMovieId(), new MovieTitle("Parasite"), userIdOne));
		tut.save(new Movie(tut.nextMovieId(), new MovieTitle("Parasite"), userIdTwo));
		tut.save(new Movie(tut.nextMovieId(), new MovieTitle("Hulk"), userIdOne));

		// when
		tut.removeMovie(new MovieId(1), userIdTwo);

		// then
		assertThat(tut.findAll(userIdOne)).hasSize(2);
	}

	@Test
	void next_id_should_be_one_higher_than_number_of_already_created_movieIds() {
		// when
		tut.nextMovieId();
		tut.nextMovieId();
		tut.nextMovieId();

		// then
		assertThat(tut.nextMovieId().getValue()).isEqualTo(4);
	}

	@Test
	void should_return_empty_when_no_matching_movieid_or_userid() {
		// given
		final UserId userIdOne = new UserId(1);
		final UserId userIdTwo = new UserId(2);
		final MovieId movieId = tut.nextMovieId();
		final Movie movie = new Movie(movieId, new MovieTitle("Parasite"), userIdOne);

		// when
		tut.save(movie);

		// then
		assertThat(tut.findBy(movieId, userIdTwo)).isEmpty();
		assertThat(tut.findBy(tut.nextMovieId(), userIdOne)).isEmpty();
	}

	@Test
	void should_return_matching_movie_title() {
		// given
		final UserId userIdOne = new UserId(1);
		tut.save(new Movie(tut.nextMovieId(), new MovieTitle("Parasite"), userIdOne));
		final MovieId movieId = tut.nextMovieId();
		tut.save(new Movie(movieId, new MovieTitle("Star Wars"), userIdOne));
		tut.save(new Movie(tut.nextMovieId(), new MovieTitle("Hulk"), userIdOne));

		// when
		final Movie movie = tut.findBy(movieId, userIdOne).get();

		// then
		assertThat(movie.getTitle()).isEqualTo(new MovieTitle("Star Wars"));
	}

}