package pl.szczesniak.dominik.whattowatch.movies.query.model;

import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@Value
public class GetMoviesByTags {

	@NonNull List<MovieTagId> tags;

	@NonNull UserId userId;

	@NonNull Integer page;

	@NonNull Integer moviesPerPage;

}
