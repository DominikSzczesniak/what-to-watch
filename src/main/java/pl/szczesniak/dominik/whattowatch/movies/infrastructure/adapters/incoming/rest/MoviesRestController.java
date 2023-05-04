package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.MoveMovieToWatchedMoviesList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.MovieDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.UserDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class MoviesRestController {

	private final MoviesService moviesService;

	@GetMapping("/api/movies")
	public List<MovieDto> findAllMoviesToWatch(@RequestBody final Integer userId) {
		return moviesService.getMoviesToWatch(new UserId(userId)).stream()
				.map(movie -> new MovieDto(movie.getTitle(), movie.getMovieId(), movie.getUserId()))
				.toList();
	}

	@PostMapping("/api/movies")
	public MovieId addMovie(@RequestBody final CreateMovieDto movieDto) {
		return moviesService.addMovieToList(AddMovieToList.builder(new MovieTitle(movieDto.getTitle()), new UserId(movieDto.userId)).build());
	}

	@DeleteMapping("/api/movies")
	public void removeMovieFromList(@RequestBody CreateMovieToRemoveDto movieToRemoveDto) {
		moviesService.removeMovieFromList(new MovieId(movieToRemoveDto.getMovieId()), new UserId(movieToRemoveDto.getUserId()));
	}

	@GetMapping("/api/movies/watchedmovies")
	public List<MovieDto> findWatchedMovies(@RequestBody final Integer userId) {
		return moviesService.getWatchedMovies(new UserId(userId)).stream()
				.map(movie -> new MovieDto(movie.getTitle(), movie.getMovieId(), movie.getUserId()))
				.toList();
	}

	@PostMapping("/api/movies/{movieId}/watchedmovies")
	public void moveMovieToWatchedList(@PathVariable Integer movieId, @RequestBody final Integer userId) {
		moviesService.moveMovieToWatchedList(new MoveMovieToWatchedMoviesList(new MovieId(movieId), new UserId(userId)));
	}

	@ExceptionHandler(MovieDoesNotExistException.class)
	public ResponseEntity<?> handleMovieDoesNotExistException() {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(UserDoesNotExistException.class)
	public ResponseEntity<?> handleUserDoesNotExistException() {
		return ResponseEntity.badRequest().build();
	}


	@Data
	private static class CreateMovieToRemoveDto {
		private Integer movieId;
		private Integer userId;
	}

	@Data
	private static class CreateMovieDto {
		private String title;
		private Integer userId;
	}

	@Value
	private static class MovieDto {
		MovieTitle title;
		MovieId movieId;
		UserId userId;
	}

}
