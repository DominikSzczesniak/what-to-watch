package pl.szczesniak.dominik.whattowatch.files.domain;

import pl.szczesniak.dominik.whattowatch.movies.domain.model.StoredFileId;

import java.io.InputStream;
import java.util.Optional;

public interface FilesStorage {

	StoredFileId store(InputStream fileContent);

	Optional<InputStream> findStoredFileContent(StoredFileId storedFileId);

	void deleteFile(StoredFileId storedFileId);

}
