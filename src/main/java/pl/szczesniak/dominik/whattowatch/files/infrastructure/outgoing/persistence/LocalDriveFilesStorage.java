package pl.szczesniak.dominik.whattowatch.files.infrastructure.outgoing.persistence;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.files.domain.FilesStorage;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.StoredFileId;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.UUID;

@Repository
public class LocalDriveFilesStorage implements FilesStorage {

	private static final String FILES_DIRECTORY = System.getProperty("user.dir") + File.separator + "files";

	private static final Logger logger = LoggerFactory.getLogger(LocalDriveFilesStorage.class);

	public LocalDriveFilesStorage() {
		createDirectoryIfDoesNotExists();
	}

	private void createDirectoryIfDoesNotExists() {
		final Path path = Path.of(LocalDriveFilesStorage.FILES_DIRECTORY);
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				logger.info("Failed to create the directory: " + LocalDriveFilesStorage.FILES_DIRECTORY);
				throw new UncheckedIOException(e);
			}
		}
	}

	@Override
	public StoredFileId store(final InputStream fileContent) {
		final UUID randomCoverId = UUID.randomUUID();
		final String fileName = randomCoverId.toString();
		final Path filePath = Path.of(FILES_DIRECTORY, fileName);
		try {
			Files.write(filePath, fileContent.readAllBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return new StoredFileId(randomCoverId);
	}

	@Override
	public Optional<InputStream> findStoredFileContent(final StoredFileId storedFileId) {
		try {
			final Path filePath = Path.of(FILES_DIRECTORY, storedFileId.getValue().toString());
			return Optional.of(new ByteArrayInputStream(Files.readAllBytes(filePath)));
		} catch (IOException e) {
			logger.info("Cover not found, an error occurred.");
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public void deleteFile(final StoredFileId storedFileId) {
		try {
			final Path filePath = Path.of(FILES_DIRECTORY, storedFileId.getValue().toString());
			FileUtils.forceDelete(new File(filePath.toString()));
		} catch (IOException e) {
			logger.info("Cover not deleted, an error occurred.");
			throw new UncheckedIOException(e);
		}
	}

}
