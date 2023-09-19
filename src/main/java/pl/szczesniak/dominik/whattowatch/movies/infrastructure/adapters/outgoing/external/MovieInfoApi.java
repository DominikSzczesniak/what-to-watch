package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external;

public interface MovieInfoApi {

	MovieInfo getPopularMovies();

	Genre getGenres();

	MovieInfo getMoviesByGenre(Long genreId);

	MovieInfo findMovieById(Long movieId);

}
