package pl.szczesniak.dominik.whattowatch.movies.domain.model.commands;

import lombok.Builder;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.CoverContentSample.createAnyCoverContent;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.CoverContentTypeSample.createAnyContentType;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.CoverFilenameSample.createAnyCoverFilename;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieIdSample.createAnyMovieId;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class SetMovieCoverSample {

	@Builder
	private static SetMovieCover build(final UserId userId,
									   final MovieId movieId,
									   final String coverFilename,
									   final String coverContentType,
									   final InputStream coverContent) {
		return new SetMovieCover(
				ofNullable(userId).orElse(createAnyUserId()),
				ofNullable(movieId).orElse(createAnyMovieId()),
				ofNullable(coverFilename).orElse(createAnyCoverFilename()),
				ofNullable(coverContentType).orElse(createAnyContentType()),
				ofNullable(coverContent).orElse(new ByteArrayInputStream(createAnyCoverContent()))
		);
	}

}
