package pl.szczesniak.dominik.whattowatch.movies.domain;

public class TestMoviesToWatchServiceConfiguration {


    static MoviesToWatchService moviesToWatchService(MoviesRepository moviesRepository, UserProvider userProvider) {
        return new MoviesToWatchServiceConfiguration().moviesToWatchService(moviesRepository, userProvider);
    }

}
