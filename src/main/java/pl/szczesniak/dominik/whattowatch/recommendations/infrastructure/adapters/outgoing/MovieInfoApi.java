package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing;

import java.util.List;

public interface MovieInfoApi {

	MovieInfoTMDBResponse getPopularMovies();

	MovieGenreTMDBResponse getGenres();

	MovieInfoTMDBResponse getMoviesByGenre(List<Long> genreId);

}
