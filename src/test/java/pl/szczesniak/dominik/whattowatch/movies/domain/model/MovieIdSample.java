package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import java.util.Random;

public class MovieIdSample {

	public static MovieId createAnyMovieId() {
		return new MovieId(new Random().nextInt(1, 99999));
	}

}
