package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkArgument;

@Embeddable
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class RecommendedMoviesId {

	private Integer value;

	public RecommendedMoviesId(final Integer value) {
		checkArgument(value > 0, "UserId value must be higher than 0");
		this.value = value;
	}

}
