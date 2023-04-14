package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public class MovieTitle {

	String value;

	public MovieTitle(final String value) {
		checkArgument(value != null, "Movie title can't be null");
		this.value = value;
	}
}
