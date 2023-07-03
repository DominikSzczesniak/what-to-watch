package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.Movie;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieComment;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FindMovieToWatchController {

	private final MoviesService moviesService;

	@GetMapping("/api/movies/{movieId}")
	public MovieDto findMovieToWatch(@RequestHeader("userId") final Integer userId, @PathVariable Integer movieId) {
		final Movie movie = moviesService.getMovie(new MovieId(movieId), new UserId(userId));
		return new MovieDto(movie.getTitle().getValue(), movieId, userId, movie.getComments());
	}

	@Value
	private static class MovieDto {
		String title;
		Integer movieId;
		Integer userId;
		List<MovieComment> comments;
	}

}
