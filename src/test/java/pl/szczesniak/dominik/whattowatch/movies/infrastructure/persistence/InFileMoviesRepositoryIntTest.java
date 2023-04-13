package pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.szczesniak.dominik.whattowatch.movies.domain.Movie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.szczesniak.dominik.whattowatch.movies.domain.Movie.recreate;

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
		tut.save(recreate(new MovieId(5), new MovieTitle("Parasite"), new UserId(1)));

		// then
		final String line = "1,5,Parasite";
		assertThat(Files.readAllLines(testMoviesFile.toPath())).contains(line);
	}

	@Test
	void user_should_save_movie() {
		// given
		final UserId userId = new UserId(1);

		// when
		tut.save(recreate(tut.nextMovieId(), new MovieTitle("Parasite"), userId));
		tut.save(recreate(tut.nextMovieId(), new MovieTitle("Hulk"), userId));

		// then
		assertThat(tut.findAll(userId)).hasSize(2);
	}

	@Test
	void user_should_delete_movie() {
		//given
		final MovieId movieId = tut.nextMovieId();
		final UserId userId = new UserId(1);

		tut.save(recreate(movieId, new MovieTitle("Parasite"), userId));
		tut.save(recreate(tut.nextMovieId(), new MovieTitle("Hulk"), userId));
		assertThat(tut.findAll(userId)).hasSize(2);

		// when
		tut.removeMovie(movieId, userId);

		// then
		assertThat(tut.findAll(userId)).hasSize(1);
	}

	@Test
	void should_not_remove_movie_when_movieid_doesnt_belong_to_userid() {
		//given
		final UserId userIdOne = new UserId(1);
		final UserId UserIdTwo = new UserId(2);
		final MovieId movieId = tut.nextMovieId();

		tut.save(recreate(movieId, new MovieTitle("Parasite"), userIdOne));
		tut.save(recreate(tut.nextMovieId(), new MovieTitle("Parasite"), UserIdTwo));

		// when
		tut.removeMovie(movieId, UserIdTwo);

		// then
		assertThat(tut.findAll(userIdOne)).hasSize(1);
	}

	@Test
	void next_movieid_should_be_one_higher_than_previously_created_movieid() {
		// given
		tut.nextMovieId();
		tut.nextMovieId();
		final MovieId lastMovieId = tut.nextMovieId();

		// when
		final MovieId nextMovieId = tut.nextMovieId();

		// then
		assertThat(nextMovieId.getValue()).isEqualTo(lastMovieId.getValue() + 1);
	}

	@Test
	void should_return_empty_when_no_matching_movieid_or_userid() {
		// given
		final UserId userIdOne = new UserId(1);
		final UserId userIdTwo = new UserId(2);
		final MovieId movieId = tut.nextMovieId();
		final Movie movie = recreate(movieId, new MovieTitle("Parasite"), userIdOne);

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
		tut.save(recreate(tut.nextMovieId(), new MovieTitle("Parasite"), userIdOne));
		final MovieId movieId = tut.nextMovieId();
		tut.save(recreate(movieId, new MovieTitle("Star Wars"), userIdOne));
		tut.save(recreate(tut.nextMovieId(), new MovieTitle("Hulk"), userIdOne));

		// when
		final Movie movie = tut.findBy(movieId, userIdOne).get();

		// then
		assertThat(movie.getTitle()).isEqualTo(new MovieTitle("Star Wars"));
	}

}