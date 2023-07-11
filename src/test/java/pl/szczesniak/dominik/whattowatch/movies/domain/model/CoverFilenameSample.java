package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import org.apache.commons.lang3.RandomStringUtils;

public class CoverFilenameSample {

	public static String createAnyCoverFilename() {
		return RandomStringUtils.randomAlphabetic(10);
	}

}
