package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.UpdateMovie;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@RequiredArgsConstructor
@RestController
public class UpdateMovieController {

	private final MoviesService moviesService;

	@PutMapping("/api/movies/{movieId}")
	public void updateMovie(@RequestHeader("userId") Integer userId, @PathVariable final Integer movieId, @RequestBody final UpdateMovieDto updateMovieDto) {
		moviesService.updateMovie(UpdateMovie.builder(new MovieId(movieId), new UserId(userId), new MovieTitle(updateMovieDto.getMovieTitle())).build());
	}

	@Data
	private static class UpdateMovieDto {
		private String movieTitle;
	}

}
