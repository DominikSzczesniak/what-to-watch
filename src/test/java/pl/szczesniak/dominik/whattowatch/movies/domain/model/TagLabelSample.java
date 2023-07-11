package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import org.apache.commons.lang3.RandomStringUtils;

public class TagLabelSample {

	public static TagLabel createAnyTagLabel() {
		return new TagLabel(RandomStringUtils.randomAlphabetic(10));
	}

}
