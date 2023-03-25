package pl.szczesniak.dominik;

import pl.szczesniak.dominik.whattowatch.movies.domain.infrastructure.persistence.InMemoryMoviesRepository;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MoviesToWatchService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MoviesToWatchServiceConfiguration;
import pl.szczesniak.dominik.whattowatch.users.domain.model.User;

public class Main {
    public static void main(String[] args) {
        MoviesToWatchService service = new MoviesToWatchServiceConfiguration().moviesToWatchService(new InMemoryMoviesRepository());
        User user = new User("Dominik");
        service.addMovieToList(user, "Parasite", user.getId());
        service.addMovieToList(user, "Star wars", user.getId());
        System.out.println(service.getMovieTitles(user));
    }
}