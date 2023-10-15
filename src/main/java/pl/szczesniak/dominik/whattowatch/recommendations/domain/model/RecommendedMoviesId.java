package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public class RecommendedMoviesId {

	Long value;

	public RecommendedMoviesId(final Long value) {
		checkArgument(value > 0, "UserId value must be higher than 0");
		this.value = value;
	}

}
