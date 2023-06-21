package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class MovieId {

	int value;

	public MovieId(final int value) {
		this.value = value;
	}

}
