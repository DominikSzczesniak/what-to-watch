package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import org.apache.commons.lang3.RandomStringUtils;

public class MovieTitleSample {

	public static MovieTitle createAnyMovieTitle() {
		return new MovieTitle(RandomStringUtils.randomAlphabetic(10));
	}

}
