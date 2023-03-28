package pl.szczesniak.dominik;

import pl.szczesniak.dominik.whattowatch.movies.domain.model.MoviesToWatchService;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MoviesToWatchServiceConfiguration;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence.InMemoryMoviesRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserService;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserServiceConfiguration;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence.InFileUserRepository;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence.InMemoryUserRepository;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceConfiguration().userService(new InFileUserRepository("userList.csv"));
        UserService userService2 = new UserServiceConfiguration().userService(new InMemoryUserRepository());
        MoviesToWatchService service = new MoviesToWatchServiceConfiguration().moviesToWatchService(new InMemoryMoviesRepository(), userService);
        userService.createUser("Grzegorz");
        userService.createUser("Kamil");
        userService2.createUser("Kamil");
        userService.createUser("Dominik");
        userService2.createUser("Dominik");
        userService.createUser("Patryk");
        userService2.createUser("Patryk");
        UserId id = userService.getUserId("Grzegorz");
        service.addMovieToList("Parasite", userService.getUserId("Grzegorz"));
        System.out.println(userService.getUserId("Grzegorz"));
        System.out.println(service.getList(id));
    }
}