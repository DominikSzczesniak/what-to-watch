package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.persistence;

import pl.szczesniak.dominik.whattowatch.files.domain.FilesStorage;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.StoredFileId;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryFilesStorage implements FilesStorage {

	private final Map<StoredFileId, byte[]> covers = new HashMap<>();

	@Override
	public StoredFileId store(final InputStream fileContent) {
		final UUID uuid = UUID.randomUUID();
		try {
			covers.put(new StoredFileId(uuid), fileContent.readAllBytes());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return new StoredFileId(uuid);
	}

	@Override
	public Optional<InputStream> findStoredFileContent(final StoredFileId storedFileId) {
		final byte[] value = covers.get(storedFileId);
		return Optional.of(new ByteArrayInputStream(value));
	}

	@Override
	public void deleteFile(final StoredFileId storedFileId) {
		covers.remove(storedFileId);
	}

}
