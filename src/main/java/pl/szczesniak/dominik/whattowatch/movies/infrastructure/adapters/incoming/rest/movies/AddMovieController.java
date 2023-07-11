package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@RequiredArgsConstructor
@RestController
public class AddMovieController {

	private final MoviesService moviesService;

	@PostMapping("/api/movies")
	public Integer addMovie(@RequestBody final CreateMovieDto movieDto) {
		return moviesService.addMovieToList(AddMovieToList.builder(new MovieTitle(movieDto.getTitle()), new UserId(movieDto.userId)).build()).getValue();
	}

	@Data
	private static class CreateMovieDto {
		private String title;
		private Integer userId;
	}

}
