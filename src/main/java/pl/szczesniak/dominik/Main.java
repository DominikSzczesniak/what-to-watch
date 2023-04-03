package pl.szczesniak.dominik;

import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesToWatchService;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesToWatchServiceConfiguration;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence.InMemoryMoviesRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserService;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserServiceConfiguration;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence.InFileUserRepository;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence.InMemoryUserRepository;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceConfiguration().userService(new InMemoryUserRepository());
        MoviesToWatchService service = new MoviesToWatchServiceConfiguration().moviesToWatchService(new InMemoryMoviesRepository(), userService);

        userService.createUser("Kamil");
        userService.createUser("Dominik");
        userService.createUser("Patryk");
        UserId id = userService.createUser("Grzegorz");
        service.addMovieToList("Parasite", id);

        System.out.println(service.getList(id));
        System.out.println("User service memory ids:");
        System.out.println(userService.findAllUsers());

        UserService userServiceFile = new UserServiceConfiguration().userService(new InFileUserRepository("userList.csv"));
        System.out.println("User service file users:");
        System.out.println(userServiceFile.findAllUsers());

    }
}