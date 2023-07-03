package pl.szczesniak.dominik.whattowatch.movies.domain;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.StoredFileId;

import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
class MovieCover {

	@NonNull private String filename;

	@NonNull private String coverContentType;

	private UUID coverId;

	MovieCover(@NonNull final String filename, @NonNull final String coverContentType) {
		this.filename = filename;
		this.coverContentType = coverContentType;
	}

	public StoredFileId getCoverId() {
		return new StoredFileId(coverId);
	}

	void setCoverId(final StoredFileId coverId) {
		this.coverId = coverId.getValue();
	}

}
