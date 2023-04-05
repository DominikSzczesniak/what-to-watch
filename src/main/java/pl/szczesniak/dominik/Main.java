package pl.szczesniak.dominik;

import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesToWatchService;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesToWatchServiceConfiguration;
import pl.szczesniak.dominik.whattowatch.movies.infrastructure.persistence.InFileMoviesRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.UserService;
import pl.szczesniak.dominik.whattowatch.users.domain.UserServiceConfiguration;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence.InFileUserRepository;

public class Main {
    public static void main(String[] args) {
//        UserServiceConfiguration userServiceConfiguration = new UserServiceConfiguration();
//        UserService userService = userServiceConfiguration.userService(new InFileUserRepository("userMain.csv"));
//        MoviesToWatchServiceConfiguration moviesToWatchServiceConfiguration = new MoviesToWatchServiceConfiguration();
//        MoviesToWatchService moviesService = moviesToWatchServiceConfiguration.moviesToWatchService(
//                new InFileMoviesRepository("movieMain.csv"), moviesToWatchServiceConfiguration.userProvider(userService));
//
//        UserId dominik = userService.createUser(new User("Dominik", new UserId(userService.nextUserId().getValue())));
//        UserId patryk = userService.createUser(new User("Patryk", new UserId(userService.nextUserId().getValue())));
//        moviesService.addMovieToList("Parasite", dominik);
//        moviesService.addMovieToList("abc", patryk);
//        moviesService.addMovieToList("Star Wars", patryk);
//        moviesService.addMovieToList("Star Wars", dominik);
//        moviesService.addMovieToList("Hobbit", dominik);
//        moviesService.addMovieToList("Titanic", patryk);
//        moviesService.addMovieToList("Kingdom", dominik);
//        System.out.println(moviesService.getList(dominik));
//        moviesService.removeMovieFromList("Hobbit", dominik);
//        moviesService.removeMovieFromList("abc", patryk);
//        System.out.println(moviesService.getList(patryk));
//        moviesService.addMovieToList("abc", patryk);
    }
}