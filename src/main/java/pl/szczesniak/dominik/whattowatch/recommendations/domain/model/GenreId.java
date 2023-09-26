package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Embeddable
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class GenreId {

	private Long value;

	public GenreId(@NonNull final Long value) {
		this.value = value;
	}

}
