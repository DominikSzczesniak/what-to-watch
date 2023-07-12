package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import org.apache.commons.lang3.RandomStringUtils;

public class CommentSample {

	public static String createAnyComment() {
		return RandomStringUtils.randomAlphabetic(10);
	}

}
