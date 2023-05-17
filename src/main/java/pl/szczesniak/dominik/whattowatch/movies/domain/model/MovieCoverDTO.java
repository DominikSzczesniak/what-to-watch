package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.NonNull;
import lombok.Value;

import java.io.InputStream;

@Value
public class MovieCoverDTO {

	@NonNull String filename;

	@NonNull String coverContentType;

	@NonNull InputStream coverContent;

}
