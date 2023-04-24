package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public class MovieTitle {

	String value;

	public MovieTitle(final String value) {
		checkArgument(value != null, "Must contain title");
		checkArgument(value.trim().length() > 0, "Title must have at least 1 character");
		checkArgument(movieTitleIsValid(value), "Title may only contain spaces, letters, numbers and characters such as: -,':");
		this.value = value;
	}

	private boolean movieTitleIsValid(final String value) {
		return value.matches("(?i)[a-z0-9][a-zA-Z0-9 \\-\\.\\'\\:]*$");
	}

}
