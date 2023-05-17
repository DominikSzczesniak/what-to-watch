package pl.szczesniak.dominik.whattowatch.movies.domain.model.commands;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.io.InputStream;

import static java.util.Objects.requireNonNull;

@Getter
@ToString
@EqualsAndHashCode
public class SetMovieCover {

	private final UserId userId;
	private final MovieId movieId;
	private final String coverFilename;
	private final String coverContentType;
	private final InputStream coverContent;

	public SetMovieCover(@NonNull final UserId userId, @NonNull final MovieId movieId,
						 @NonNull final String coverFilename, @NonNull final String coverContentType, final InputStream coverContent) {
		this.userId = userId;
		this.movieId = movieId;
		this.coverFilename = coverFilename;
		this.coverContentType = coverContentType;
		this.coverContent = requireNonNull(coverContent);
	}

}