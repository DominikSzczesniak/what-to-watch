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
				"Incorrect movie title. Title must be at least 1 character long and may only contain spaces, letters, numbers and characters such as: -,':");
		this.value = value;
	}

	static boolean movieTitleIsValid(final String value) {
		return value.matches("(?i)[a-z0-9][a-zA-Z0-9 \\-\\.\\'\\:]*$");
	}

}
