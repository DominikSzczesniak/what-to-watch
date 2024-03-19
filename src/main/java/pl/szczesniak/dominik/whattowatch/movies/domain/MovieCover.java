package pl.szczesniak.dominik.whattowatch.movies.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.StoredFileId;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
class MovieCover {

	@Id
	private UUID coverId;

	@NonNull
	private String filename;

	@NonNull
	private String coverContentType;

	MovieCover(@NonNull final String filename, @NonNull final String coverContentType, @NonNull final UUID coverId) {
		this.filename = filename;
		this.coverContentType = coverContentType;
		this.coverId = coverId;
	}

	StoredFileId getCoverId() {
		return new StoredFileId(coverId);
	}

}
