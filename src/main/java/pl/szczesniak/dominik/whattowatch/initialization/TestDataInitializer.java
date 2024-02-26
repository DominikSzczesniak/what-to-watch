package pl.szczesniak.dominik.whattowatch.initialization;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.MoveMovieToWatchedMoviesList;
import pl.szczesniak.dominik.whattowatch.users.domain.UserFacade;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.commands.CreateUser;

@Component
@RequiredArgsConstructor
@Profile("test")
public class TestDataInitializer {

	private final MoviesFacade moviesFacade;
	private final UserFacade userFacade;

	@PostConstruct
	public void fillDatabase() {
		final UserId userId = userFacade.createUser(new CreateUser(new Username("Dominik"), new UserPassword("123")));

		moviesFacade.addMovieToList(AddMovieToList.builder(new MovieTitle("Star Wars"), userId).build());
		final MovieId parasite = moviesFacade.addMovieToList(AddMovieToList.builder(new MovieTitle("Parasite"), userId).build());
		moviesFacade.addMovieToList(AddMovieToList.builder(new MovieTitle("Hulk"), userId).build());
		moviesFacade.addMovieToList(AddMovieToList.builder(new MovieTitle("Grandfather"), userId).build());
		final MovieId pulpFiction = moviesFacade.addMovieToList(AddMovieToList.builder(new MovieTitle("Pulp Fiction"), userId).build());
		moviesFacade.addMovieToList(AddMovieToList.builder(new MovieTitle("Lord of the Rings"), userId).build());
		moviesFacade.addMovieToList(AddMovieToList.builder(new MovieTitle("Inception"), userId).build());
		moviesFacade.addMovieToList(AddMovieToList.builder(new MovieTitle("Avatar"), userId).build());
		moviesFacade.addMovieToList(AddMovieToList.builder(new MovieTitle("Interstellar"), userId).build());

		moviesFacade.moveMovieToWatchedList(new MoveMovieToWatchedMoviesList(parasite, userId));
		moviesFacade.moveMovieToWatchedList(new MoveMovieToWatchedMoviesList(pulpFiction, userId));
	}

}
