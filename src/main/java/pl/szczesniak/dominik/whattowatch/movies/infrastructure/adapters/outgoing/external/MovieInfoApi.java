package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external;

public interface MovieInfoApi {

	MovieInfoResponse getPopularMovies();

	GenresResponse getGenres();

	MovieInfoResponse getMoviesByGenre(Long genreId);

//	MovieInfoResponse findMovieById(Long movieId);

}
