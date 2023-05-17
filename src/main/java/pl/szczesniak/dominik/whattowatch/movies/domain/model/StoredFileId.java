package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class StoredFileId {

	@NonNull UUID value;

}
