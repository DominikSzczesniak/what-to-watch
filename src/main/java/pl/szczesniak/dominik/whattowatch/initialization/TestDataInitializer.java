package pl.szczesniak.dominik.whattowatch.initialization;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.MoveMovieToWatchedMoviesList;
import pl.szczesniak.dominik.whattowatch.users.domain.UserService;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.commands.CreateUser;

@Component
@RequiredArgsConstructor
@Profile("test")
public class TestDataInitializer {

	private final MoviesService moviesService;
	private final UserService userService;

	@PostConstruct
	public void fillDatabase() {
		final UserId userId = userService.createUser(new CreateUser(new Username("Dominik"), new UserPassword("123")));

		moviesService.addMovieToList(AddMovieToList.builder(new MovieTitle("Star Wars"), userId).build());
		final MovieId parasite = moviesService.addMovieToList(AddMovieToList.builder(new MovieTitle("Parasite"), userId).build());
		moviesService.addMovieToList(AddMovieToList.builder(new MovieTitle("Hulk"), userId).build());
		moviesService.addMovieToList(AddMovieToList.builder(new MovieTitle("Grandfather"), userId).build());
		final MovieId pulpFiction = moviesService.addMovieToList(AddMovieToList.builder(new MovieTitle("Pulp Fiction"), userId).build());
		moviesService.addMovieToList(AddMovieToList.builder(new MovieTitle("Lord of the Rings"), userId).build());
		moviesService.addMovieToList(AddMovieToList.builder(new MovieTitle("Inception"), userId).build());
		moviesService.addMovieToList(AddMovieToList.builder(new MovieTitle("Avatar"), userId).build());
		moviesService.addMovieToList(AddMovieToList.builder(new MovieTitle("Interstellar"), userId).build());

		moviesService.moveMovieToWatchedList(new MoveMovieToWatchedMoviesList(parasite, userId));
		moviesService.moveMovieToWatchedList(new MoveMovieToWatchedMoviesList(pulpFiction, userId));
	}

}
