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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pl.szczesniak.dominik.whattowatch.movies.domain.Movie.recreate;

@RequiredArgsConstructor
public class InFileMoviesRepository implements MoviesRepository {

	private final String fileName;
	private final int INDEX_WITH_USER_ID_NUMBER_IN_CSV = 0;
	private final int INDEX_WITH_MOVIE_ID_NUMBER_IN_CSV = 1;
	private final int INDEX_WITH_MOVIE_TITLE_NUMBER_IN_CSV = 2;

	@Override
	public void save(final Movie movie) {
		createFile();

		try (FileWriter fw = new FileWriter(fileName, true)) {
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(movie.getUserId().getValue() + "," + movie.getMovieId().getValue() + "," + movie.getTitle());
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public MovieId nextMovieId() {
		createFile();
		String lastLine = "";
		int id = 1;
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				lastLine = line;
				List<String> listLine = Arrays.stream(lastLine.split("[,]")).toList();
				id = Integer.parseInt(listLine.get(INDEX_WITH_MOVIE_ID_NUMBER_IN_CSV)) + 1;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return new MovieId(id);
	}

	@Override
	public List<Movie> findAll(final UserId userId) {
		List<Movie> movieList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				List<String> listLine = Arrays.stream(line.split("[,]")).toList();
				if (Integer.parseInt(listLine.get(INDEX_WITH_USER_ID_NUMBER_IN_CSV)) == userId.getValue())
					movieList.add(recreate(new MovieId(Integer.parseInt(listLine.get(INDEX_WITH_MOVIE_ID_NUMBER_IN_CSV))),
							listLine.get(INDEX_WITH_MOVIE_TITLE_NUMBER_IN_CSV),
							userId));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return movieList;
	}

	@Override
	public void removeMovie(final MovieId movieId, final UserId userId) {
		String tempFile = "temp.csv";
		File oldFile = new File(fileName);
		File newFile = new File(tempFile);

		String currentLine;
		try {
			FileWriter fw = new FileWriter(tempFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);

			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);

			while ((currentLine = br.readLine()) != null) {
				List<String> listLine = Arrays.stream(currentLine.split("[,]")).toList();
				if (Integer.parseInt(listLine.get(INDEX_WITH_MOVIE_ID_NUMBER_IN_CSV)) != movieId.getValue()) {
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
			File dump = new File(fileName);
			newFile.renameTo(dump);

		} catch (Exception e) {
			System.out.println("didn't work");
		}
	}

	private void createFile() {
		try {
			File myObj = new File(fileName);
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

}