package pl.szczesniak.dominik;

import pl.szczesniak.dominik.videos.domain.model.movie.Movie;
import pl.szczesniak.dominik.videos.domain.model.movie.MoviesToWatchService;
import pl.szczesniak.dominik.videos.domain.model.User;

public class Main {
    public static void main(String[] args) {
        MoviesToWatchService service = new MoviesToWatchService();
        User one = new User();
        service.addMovieToList(one, new Movie("Parasite"));
        service.addMovieToList(one, new Movie("Star Wars"));
        System.out.println(service.getList(one));
    }
}