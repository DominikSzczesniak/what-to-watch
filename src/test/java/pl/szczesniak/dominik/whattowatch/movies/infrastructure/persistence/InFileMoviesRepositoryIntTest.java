package pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.szczesniak.dominik.whattowatch.movies.domain.Movie;
import pl.szczesniak.dominik.whattowatch.movies.domain.MovieSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.InFileMoviesRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieIdSample.createAnyMovieId;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

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
		final List<Movie> movies = tut.findAll(new UserId(1));

		// then
		assertThat(movies.size()).isEqualTo(3);
	}

	@Test
	void should_return_correct_movie_id_when_asked_for_next_movie_id_from_given_file() {
		// when
		tut = new InFileMoviesRepository(existingMoviesDbFilepath, existingMoviesIdDbFilepath);

		// then
		assertThat(tut.findNextMovieId()).isEqualTo(new MovieId(8));
	}

	@Test
	void should_save_movie_correctly_in_file() throws IOException {
		// given
		final File testMoviesFile = new File(testFileMovies, "testMovie.csv");
		tut = new InFileMoviesRepository(testMoviesFile.getAbsolutePath(), testFileId.getAbsolutePath() + testFileId.getName());

		// when
		tut.save(MovieSample.builder().movieId(new MovieId(5)).movieTitle(new MovieTitle("Parasite")).userId(new UserId(1)).build());

		// then
		final String line = "1,5,Parasite";
		assertThat(Files.readAllLines(testMoviesFile.toPath())).contains(line);
	}

	@Test
	void user_should_save_movie() {
		// given
		final UserId userId = createAnyUserId();

		// when
		tut.save(MovieSample.builder().userId(userId).build());
		tut.save(MovieSample.builder().userId(userId).build());

		// then
		assertThat(tut.findAll(userId)).hasSize(2);
	}

	@Test
	void user_should_delete_movie() {
		//given
		final MovieId movieId = createAnyMovieId();
		final UserId userId = createAnyUserId();

		tut.save(MovieSample.builder().movieId(movieId).userId(userId).build());
		tut.save(MovieSample.builder().userId(userId).build());
		assertThat(tut.findAll(userId)).hasSize(2);

		// when
		tut.removeMovie(movieId, userId);

		// then
		assertThat(tut.findAll(userId)).hasSize(1);
	}

	@Test
	void should_not_remove_movie_when_movieid_does_not_belong_to_userid() {
		//given
		final UserId userIdOne = createAnyUserId();
		final UserId userIdTwo = createAnyUserId();
		final MovieId movieId = createAnyMovieId();

		tut.save(MovieSample.builder().userId(userIdOne).movieId(movieId).build());
		tut.save(MovieSample.builder().userId(userIdTwo).build());

		// when
		tut.removeMovie(movieId, userIdTwo);

		// then
		assertThat(tut.findAll(userIdOne)).hasSize(1);
	}

	@Test
	void next_movieid_should_be_one_higher_than_previously_created_movieid() {
		// given
		final MovieId lastMovieId = tut.nextMovieId();

		// when
		final MovieId nextMovieId = tut.nextMovieId();

		// then
		assertThat(nextMovieId.getValue()).isEqualTo(lastMovieId.getValue() + 1);
	}

	@Test
	void should_find_saved_movie_when_movie_belongs_to_user_otherwise_should_return_empty() {
		// given
		final UserId userIdOne = createAnyUserId();
		final UserId userIdTwo = createAnyUserId();
		final MovieId movieId = createAnyMovieId();
		final Movie movie = MovieSample.builder().movieId(movieId).userId(userIdOne).build();

		// when
		tut.save(movie);

		// then
		assertThat(tut.findBy(movieId, userIdTwo)).isEmpty();
		assertThat(tut.findBy(tut.nextMovieId(), userIdOne)).isEmpty();
		assertThat(tut.findBy(tut.nextMovieId(), userIdTwo)).isEmpty();
		assertThat(tut.findBy(movieId, userIdOne)).isPresent();
	}

	@Test
	void should_return_matching_movie() {
		// given
		final UserId userIdOne = createAnyUserId();
		final MovieId movieId = createAnyMovieId();
		final Movie createdMovie = MovieSample.builder().movieId(movieId).userId(userIdOne).build();
		tut.save(createdMovie);

		// when
		final Movie foundMovie = tut.findBy(movieId, userIdOne).get();

		// then
		assertThat(foundMovie).isEqualTo(createdMovie);
	}

}