package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import lombok.Builder;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.util.Optional.ofNullable;

public class MovieInfoSample {

	@Builder
	private static MovieInfo build(final String title, final String overview, final List<MovieGenre> genres) {
		return new MovieInfo(
				ofNullable(genres).orElse(getRandomGenres()),
				ofNullable(title).orElse(generateRandomTitle()),
				ofNullable(overview).orElse(generateRandomOverview())
		);
	}

	private static List<MovieGenre> getRandomGenres() {
		final List<MovieGenre> allGenres = Arrays.asList(MovieGenre.values());
		final List<MovieGenre> randomGenres = new ArrayList<>();
		final Random random = new Random();

		int numGenres = 3;
		for (int i = 0; i < numGenres; i++) {
			int randomIndex = random.nextInt(allGenres.size());
			randomGenres.add(allGenres.get(randomIndex));
		}

		return randomGenres;
	}

	private static String generateRandomTitle() {
		return RandomStringUtils.randomAlphabetic(10);
	}

	private static String generateRandomOverview() {
		return RandomStringUtils.randomAlphabetic(10);
	}

}
