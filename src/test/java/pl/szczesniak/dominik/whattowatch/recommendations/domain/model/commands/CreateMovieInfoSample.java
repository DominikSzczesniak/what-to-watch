package pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands;

import lombok.Builder;
import org.apache.commons.lang3.RandomStringUtils;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoApis;

import java.util.List;
import java.util.Random;

import static java.util.Optional.ofNullable;

public class CreateMovieInfoSample {

	@Builder
	public static MovieInfo build(final List<MovieGenre> genres, final String overview, final String title, final Integer externalId) {
		return new MovieInfo(
				genres,
				ofNullable(overview).orElse(generateRandomString()),
				ofNullable(title).orElse(generateRandomString()),
				ofNullable(externalId).orElse(generateRandomNumber()),
				MovieInfoApis.TMDB
		);
	}

	private static String generateRandomString() {
		return RandomStringUtils.randomAlphabetic(10);
	}

	private static int generateRandomNumber() {
		return new Random().nextInt(1, 1000000000);
	}

}
