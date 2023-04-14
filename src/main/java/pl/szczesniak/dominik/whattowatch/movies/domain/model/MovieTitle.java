package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public class MovieTitle {

	String value;

	public MovieTitle(final String value) {
		checkArgument(
				value != null
				&& value.length() > 0
				&& movieTitleIsValid(value),
				"Incorrect movie title. Title may only contain letters, numbers and characters such as: -,':");
		this.value = value;
	}

	private static boolean movieTitleIsValid(final String value) {
		return value.matches("(?i)[a-z][a-zA-Z0-9 \\-\\.\\'\\:]*$");
	}

}
