package pl.szczesniak.dominik.whattowatch.movies.domain.model;

public class MoviesToWatchServiceConfiguration {

    public  MoviesToWatchService moviesToWatchService(final MoviesRepository moviesRepository) {
        return new MoviesToWatchService(moviesRepository);
    }

}
