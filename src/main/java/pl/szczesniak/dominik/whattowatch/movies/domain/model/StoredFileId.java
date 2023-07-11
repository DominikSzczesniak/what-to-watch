package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class StoredFileId {

	@NonNull UUID value;

	public StoredFileId(@NonNull final UUID value) {
		this.value = value;
	}

}
