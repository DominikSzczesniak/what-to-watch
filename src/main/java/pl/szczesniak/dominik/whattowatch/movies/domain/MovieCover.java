package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.StoredFileId;

@Value
class MovieCover {

	@NonNull String filename;

	@NonNull String coverContentType;

	@NonNull StoredFileId coverId;

}
