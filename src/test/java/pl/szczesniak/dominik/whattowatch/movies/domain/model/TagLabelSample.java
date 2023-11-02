package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import org.apache.commons.lang3.RandomStringUtils;

public class TagLabelSample {

	public static MovieTagLabel createAnyTagLabel() {
		return new MovieTagLabel(RandomStringUtils.randomAlphabetic(10));
	}

}
