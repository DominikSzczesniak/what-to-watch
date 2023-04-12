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
		//when
		tut.save(new Movie(tut.nextMovieId(), new MovieTitle("Parasite"), new UserId(1)));
		tut.save(new Movie(tut.nextMovieId(), new MovieTitle("Hulk"), new UserId(1)));

		// then
		assertThat(tut.findAll(new UserId(1))).hasSize(2);
	}

	@Test
	void user_should_delete_movie() {
		//given
		tut.save(new Movie(tut.nextMovieId(), new MovieTitle("Parasite"), new UserId(1)));
		tut.save(new Movie(tut.nextMovieId(), new MovieTitle("Hulk"), new UserId(1)));

		// when
		tut.removeMovie(new MovieId(1), new UserId(1));

		// then
		assertThat(tut.findAll(new UserId(1))).hasSize(1);
	}

	@Test
	void should_not_remove_movie_when_movieid_doesnt_belong_to_userid() {
		//given
		tut.save(new Movie(tut.nextMovieId(), new MovieTitle("Parasite"), new UserId(1)));
		tut.save(new Movie(tut.nextMovieId(), new MovieTitle("Parasite"), new UserId(2)));
		tut.save(new Movie(tut.nextMovieId(), new MovieTitle("Hulk"), new UserId(1)));

		// when
		tut.removeMovie(new MovieId(1), new UserId(2));

		// then
		assertThat(tut.findAll(new UserId(1))).hasSize(2);
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

}