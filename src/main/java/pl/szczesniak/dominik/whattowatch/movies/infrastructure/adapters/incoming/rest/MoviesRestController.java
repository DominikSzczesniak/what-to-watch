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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.MoveMovieToWatchedMoviesList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.UpdateMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.MovieDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.exceptions.UserDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MoviesRestController {

	private final MoviesService moviesService;

	@GetMapping("/api/movies")
	public List<MovieDto> findAllMoviesToWatch(@RequestHeader("userId") final Integer userId) {
		return moviesService.getMoviesToWatch(new UserId(userId)).stream()
				.map(movie -> new MovieDto(movie.getTitle().getValue(), movie.getMovieId().getValue(), movie.getUserId().getValue()))
				.toList();
	}

	@PostMapping("/api/movies")
	public Integer addMovie(@RequestBody final CreateMovieDto movieDto) {
		return moviesService.addMovieToList(AddMovieToList.builder(new MovieTitle(movieDto.getTitle()), new UserId(movieDto.userId)).build()).getValue();
	}

	@DeleteMapping("/api/movies/{movieId}")
	public ResponseEntity<?> removeMovieFromList(@RequestHeader("userId") Integer userId, @PathVariable Integer movieId) {
		moviesService.removeMovieFromList(new MovieId(movieId), new UserId(userId));
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/api/movies/{movieId}")
	public void updateMovie(@RequestHeader("userId") Integer userId, @PathVariable final Integer movieId, @RequestBody final UpdateMovieDto updateMovieDto) {
		moviesService.updateMovie(new UpdateMovie(new MovieId(movieId), new UserId(userId), new MovieTitle(updateMovieDto.getMovieTitle())));
	}

	@GetMapping("/api/movies/watched")
	public List<MovieDto> findWatchedMovies(@RequestHeader("userId") Integer userId) {
		return moviesService.getWatchedMovies(new UserId(userId)).stream()
				.map(movie -> new MovieDto(movie.getTitle().getValue(), movie.getMovieId().getValue(), movie.getUserId().getValue()))
				.toList();
	}

	@PostMapping("/api/movies/{movieId}/watched")
	public void moveMovieToWatchedList(@PathVariable Integer movieId, @RequestHeader("userId") Integer userId) {
		moviesService.moveMovieToWatchedList(new MoveMovieToWatchedMoviesList(new MovieId(movieId), new UserId(userId)));
	}

	@ExceptionHandler(MovieDoesNotExistException.class)
	public ResponseEntity<?> handleMovieDoesNotExistException() {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArgumentException() {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(UserDoesNotExistException.class)
	public ResponseEntity<?> handleUserDoesNotExistException() {
		return ResponseEntity.badRequest().build();
	}

	@Data
	private static class CreateMovieDto {
		private String title;
		private Integer userId;
	}

	@Value
	private static class MovieDto {
		String title;
		Integer movieId;
		Integer userId;
	}

	@Data
	private static class UpdateMovieDto {
		private String movieTitle;
	}

}
