package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CommentId {

	UUID value;

	public CommentId(final UUID value) {
		this.value = value;
	}

}
