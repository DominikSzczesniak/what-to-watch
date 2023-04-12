package pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.movies.domain.Movie;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesRepository;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pl.szczesniak.dominik.whattowatch.movies.domain.Movie.recreate;

@RequiredArgsConstructor
public class InFileMoviesRepository implements MoviesRepository {

	private final String fileNameOfUsers;
	private final String moviesIdFileName;
	private static final int INDEX_WITH_USER_ID_NUMBER_IN_CSV = 0;
	private static final int INDEX_WITH_MOVIE_ID_NUMBER_IN_CSV = 1;
	private static final int INDEX_WITH_MOVIE_TITLE_NUMBER_IN_CSV = 2;
	private static final int ID_OF_FIRST_CREATED_MOVIE_EVER = 1;

	@Override
	public void save(final Movie movie) {
		createFile();
		try (final FileWriter fw = new FileWriter(fileNameOfUsers, true)) {
			final BufferedWriter bw = new BufferedWriter(fw);
			bw.write(movie.getUserId().getValue() + "," + movie.getMovieId().getValue() + "," + movie.getTitle());
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public List<Movie> findAll(final UserId userId) {
		final List<Movie> movieList = new ArrayList<>();
		try (final BufferedReader br = new BufferedReader(new FileReader(fileNameOfUsers))) {
			String line;
			while ((line = br.readLine()) != null) {
				final List<String> listLine = Arrays.stream(line.split("[,]")).toList();
				if (Integer.parseInt(listLine.get(INDEX_WITH_USER_ID_NUMBER_IN_CSV)) == userId.getValue())
					movieList.add(recreate(
							new MovieId(Integer.parseInt(listLine.get(INDEX_WITH_MOVIE_ID_NUMBER_IN_CSV))),
							new MovieTitle(listLine.get(INDEX_WITH_MOVIE_TITLE_NUMBER_IN_CSV)),
							userId)
					);
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return movieList;
	}

	@Override
	public void removeMovie(final MovieId movieId, final UserId userId) {
		final String tempFile = "temp.csv";
		final File oldFile = new File(fileNameOfUsers);
		final File newFile = new File(tempFile);

		String currentLine;
		try {
			final FileWriter fw = new FileWriter(tempFile, true);
			final BufferedWriter bw = new BufferedWriter(fw);
			final PrintWriter pw = new PrintWriter(bw);
			final FileReader fr = new FileReader(fileNameOfUsers);
			final BufferedReader br = new BufferedReader(fr);

			while ((currentLine = br.readLine()) != null) {
				final List<String> listLine = Arrays.stream(currentLine.split("[,]")).toList();
				if (MovieIdOrUserIdDontMatch(movieId, userId, listLine)) {
					pw.println(currentLine);
				}
				if (!movieIdHasThisUserId(movieId, userId, listLine)) {
					System.out.println("Didnt remove movie");
				}
			}

			closeAll(fw, bw, pw, fr, br);

			renameFile(oldFile, fileNameOfUsers, newFile);

		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private void renameFile(final File oldFile, final String fileNameOfUsers, final File newFile) {
		oldFile.delete();
		final File dump = new File(fileNameOfUsers);
		newFile.renameTo(dump);
	}

	private static boolean MovieIdOrUserIdDontMatch(final MovieId movieId, final UserId userId, final List<String> listLine) {
		return Integer.parseInt(listLine.get(INDEX_WITH_MOVIE_ID_NUMBER_IN_CSV)) != movieId.getValue()
				|| Integer.parseInt(listLine.get(INDEX_WITH_USER_ID_NUMBER_IN_CSV)) != userId.getValue();
	}

	private static boolean movieIdHasThisUserId(final MovieId movieId, final UserId userId, final List<String> listLine) {
		return Integer.parseInt(listLine.get(INDEX_WITH_USER_ID_NUMBER_IN_CSV)) != userId.getValue()
				&& Integer.parseInt(listLine.get(INDEX_WITH_MOVIE_ID_NUMBER_IN_CSV)) == movieId.getValue();
	}

	@Override
	public MovieId nextMovieId() {
		createFile();
		final MovieId movieId = findNextMovieId();
		overwriteMovieIdFile(movieId);
		return movieId;
	}

	MovieId findNextMovieId() {
		int id = ID_OF_FIRST_CREATED_MOVIE_EVER;
		try (final BufferedReader br = new BufferedReader(new FileReader(moviesIdFileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				id = Integer.parseInt(line) + 1;
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return new MovieId(id);
	}

	private void overwriteMovieIdFile(final MovieId movieId) {
		final String tempFile = "temp.csv";
		final File oldFile = new File(moviesIdFileName);
		final File newFile = new File(tempFile);

		try {
			final FileWriter fw = new FileWriter(tempFile, true);
			final BufferedWriter bw = new BufferedWriter(fw);
			final PrintWriter pw = new PrintWriter(bw);
			final FileReader fr = new FileReader(moviesIdFileName);
			final BufferedReader br = new BufferedReader(fr);

			pw.println(movieId.getValue());

			closeAll(fw, bw, pw, fr, br);

			renameFile(oldFile, moviesIdFileName, newFile);

		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private static void closeAll(final FileWriter fw, final BufferedWriter bw, final PrintWriter pw, final FileReader fr, final BufferedReader br) throws IOException {
		pw.flush();
		pw.close();
		fr.close();
		br.close();
		bw.close();
		fw.close();
	}

	private void createFile() {
		try {
			final File myObj = new File(fileNameOfUsers);
			final File myObjTwo = new File(moviesIdFileName);
			if (myObj.createNewFile()) {
				System.out.println("Movies file created: " + myObj.getName());
			}
			if (myObjTwo.createNewFile()) {
				System.out.println("Movies id file created: " + myObjTwo.getName());
			}
		} catch (IOException e) {
			System.out.println("An error occurred, file not created");
			throw new UncheckedIOException(e);
		}
	}

}