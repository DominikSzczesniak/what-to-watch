package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Embeddable
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TagId {

	private UUID value;

	public TagId(final UUID value) {
		requireNonNull(value);
		this.value = value;
	}

}
