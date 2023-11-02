package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static java.util.Objects.requireNonNull;

@Embeddable
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MovieTagLabel {

	private String value;

	public MovieTagLabel(final String value) {
		requireNonNull(value);
		this.value = value;
	}
}
