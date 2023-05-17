package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import org.apache.commons.lang3.RandomStringUtils;

public class CoverContentTypeSample {

	public static String createAnyContentType() {
		return RandomStringUtils.randomAlphabetic(10);
	}

}
