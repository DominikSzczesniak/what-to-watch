package pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.movies.domain.Movie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesRepository;
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

	private final String fileName;
	private static final int INDEX_WITH_USER_ID_NUMBER_IN_CSV = 0;
	private static final int INDEX_WITH_MOVIE_ID_NUMBER_IN_CSV = 1;
	private static final int INDEX_WITH_MOVIE_TITLE_NUMBER_IN_CSV = 2;

	@Override
	public void save(final Movie movie) {
		createFile();
		try (final FileWriter fw = new FileWriter(fileName, true)) {
			final BufferedWriter bw = new BufferedWriter(fw);
			bw.write(movie.getUserId().getValue() + "," + movie.getMovieId().getValue() + "," + movie.getTitle());
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public MovieId nextMovieId() {
		createFile();
		String lastLine = "";
		int id = 1;
		try (final BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				lastLine = line;
				final List<String> listLine = Arrays.stream(lastLine.split("[,]")).toList();
				id = Integer.parseInt(listLine.get(INDEX_WITH_MOVIE_ID_NUMBER_IN_CSV)) + 1;
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return new MovieId(id);
	}

	@Override
	public List<Movie> findAll(final UserId userId) {
		final List<Movie> movieList = new ArrayList<>();
		try (final BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				final List<String> listLine = Arrays.stream(line.split("[,]")).toList();
				if (Integer.parseInt(listLine.get(INDEX_WITH_USER_ID_NUMBER_IN_CSV)) == userId.getValue())
					movieList.add(recreate(new MovieId(Integer.parseInt(listLine.get(INDEX_WITH_MOVIE_ID_NUMBER_IN_CSV))),
							listLine.get(INDEX_WITH_MOVIE_TITLE_NUMBER_IN_CSV),
							userId));
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return movieList;
	}

	@Override
	public void removeMovie(final MovieId movieId, final UserId userId) {
		final String tempFile = "temp.csv";
		final File oldFile = new File(fileName);
		final File newFile = new File(tempFile);

		String currentLine;
		try {
			final FileWriter fw = new FileWriter(tempFile, true);
			final BufferedWriter bw = new BufferedWriter(fw);
			final PrintWriter pw = new PrintWriter(bw);

			final FileReader fr = new FileReader(fileName);
			final BufferedReader br = new BufferedReader(fr);

			while ((currentLine = br.readLine()) != null) {
				List<String> listLine = Arrays.stream(currentLine.split("[,]")).toList();
				if (Integer.parseInt(listLine.get(INDEX_WITH_MOVIE_ID_NUMBER_IN_CSV)) != movieId.getValue()
						|| Integer.parseInt(listLine.get(INDEX_WITH_USER_ID_NUMBER_IN_CSV)) != userId.getValue()) {
					pw.println(currentLine);
				}
			}

			pw.flush();
			pw.close();
			fr.close();
			br.close();
			bw.close();
			fw.close();

			oldFile.delete();
			final File dump = new File(fileName);
			newFile.renameTo(dump);

		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private void createFile() {
		try {
			File myObj = new File(fileName);
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			}
		} catch (IOException e) {
			System.out.println("An error occurred, file not created");
			throw new UncheckedIOException(e);
		}
	}

}