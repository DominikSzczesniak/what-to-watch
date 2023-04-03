package pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.movies.domain.Movie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class InFileMoviesRepository implements MoviesRepository {

    private final UserId userId;
    private final String fileName = getFileName();
    private final int INDEX_WITH_ID_NUMBER_IN_CSV = 0;

    @Override
    public void save(final Movie movie) {
        createFile();

        try (FileWriter fw = new FileWriter(fileName, true)) {
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(movie.getMovieId().getId() + "," + movie.getTitle());
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int nextMovieId() {
        String lastLine = "";
        int id = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lastLine = line;
                List<String> listLine = Arrays.stream(lastLine.split("[,]")).toList();
                id = Integer.parseInt(listLine.get(INDEX_WITH_ID_NUMBER_IN_CSV)) + 1;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    @Override
    public List<Movie> findAll(final UserId userId) {
        List<Movie> listLine = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listLine;
    }

    @Override
    public void removeMovie(final MovieId movieId) {
        int lineInProgress = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineInProgress++;
                List<String> listLine = Arrays.stream(line.split("[,]")).toList();
                if (Integer.parseInt(listLine.get(INDEX_WITH_ID_NUMBER_IN_CSV)) == movieId.getId()) {

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createFile() {
        try {
            File myObj = new File(fileName);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return fileName;
    }

    private String getFileName() {
        if (userId != null) {
            return userId.getId() + ".csv";
        }
        return "";
    }

}
