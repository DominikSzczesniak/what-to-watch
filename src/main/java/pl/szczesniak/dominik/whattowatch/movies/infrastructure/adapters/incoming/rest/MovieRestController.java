package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class MovieRestController {

	private final MoviesService moviesService;

	@GetMapping("/api/movie/{userId}")
	public List<MovieDto> findAllMovies(@PathVariable final Integer userId) {
		return moviesService.getMoviesToWatch(new UserId(userId)).stream()
				.map(movie -> new MovieDto(movie.getTitle(), movie.getMovieId(), movie.getUserId()))
				.toList();
	}

	@PostMapping("/api/movie")
	public MovieId addMovie(@RequestBody CreateMovieDto movieDto) {
		return moviesService.addMovieToList(AddMovieToList.builder(new MovieTitle(movieDto.getTitle()), new UserId(Integer.parseInt(movieDto.getUserId()))).build());
	}

	@DeleteMapping("/api/movie/{movieId}/{userId}")
	public void removeMovieFromList(@PathVariable final Integer movieId, @PathVariable final Integer userId) {
		moviesService.removeMovieFromList(new MovieId(movieId), new UserId(userId));
	}

	@PutMapping("/api/movie/{movieId}")
	public void updateMovieTitle(@PathVariable final Integer movieId, @RequestBody final UpdateMovieDto updateMovieDto) {
		moviesService.updateMovie(new MovieId(movieId), new UserId(updateMovieDto.getUserId()), new MovieTitle(updateMovieDto.getTitle()));
	}


	@Data
	private static class CreateMovieDto {
		private String title;
		private String userId;
	}

	@Value
	private static class MovieDto {
		MovieTitle title;
		MovieId movieId;
		UserId userId;
	}

	@Value
	private static class UpdateMovieDto {
		String title;
		Integer userId;
	}
}
