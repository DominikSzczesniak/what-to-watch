package pl.szczesniak.dominik;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserService;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserServiceConfiguration;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence.InFileUsersRepository;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceConfiguration().userService(new InFileUsersRepository("userList.csv"));
//        MoviesToWatchService service = new MoviesToWatchServiceConfiguration().moviesToWatchService(new InMemoryMoviesRepository(), userService);
        userService.createUser("Grzegorz");
//        service.addMovieToList(userService.createUser("Grzegorz"), "Parasite", userService.getUserId("Grzegorz"));
        System.out.println(userService.getUserId("Grzegorz"));
    }
}