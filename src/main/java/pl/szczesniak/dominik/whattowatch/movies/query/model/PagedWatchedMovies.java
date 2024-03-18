package pl.szczesniak.dominik.whattowatch.movies.query.model;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class PagedWatchedMovies {

	@NonNull List<WatchedMovieQueryResult> movies;

	@NonNull Integer page;

	@NonNull Integer totalPages;

	@NonNull Integer totalMovies;

}
