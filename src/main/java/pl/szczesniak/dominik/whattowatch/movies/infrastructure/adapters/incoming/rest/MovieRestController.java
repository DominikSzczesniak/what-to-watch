package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.Movie;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MovieRestController {

	private final MoviesService moviesService;

	@GetMapping("/movie")
	public List<MovieDto> findAllMovies(@RequestParam final String id) {
		return moviesService.getMoviesToWatch(new UserId(Integer.parseInt(id))).stream()
				.map(movie -> new MovieDto(movie.getTitle(), movie.getMovieId(), movie.getUserId()))
				.toList();
	}

	@PostMapping("/movie")
	public MovieId addMovie(@RequestBody CreateMovieDto movieDto) {
		return moviesService.addMovieToList(AddMovieToList.builder(new MovieTitle(movieDto.getTitle()), new UserId(Integer.parseInt(movieDto.getUserId()))).build());
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

}
