package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.console;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.AddMovieToList;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.domain.UserService;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

@RequiredArgsConstructor
//@Component
public class MySpringConsoleRunner implements CommandLineRunner {

	private final UserService userService;
	private final MoviesService moviesService;

	@Override
	public void run(final String... args) throws Exception {
		final UserId agnieszka = userService.createUser(new Username("Agnieszka"), new UserPassword("asd"));
		moviesService.addMovieToList(AddMovieToList.builder(new MovieTitle("Parasite"), agnieszka).build());

		System.out.println(moviesService.getMoviesToWatch(agnieszka));
	}

}

