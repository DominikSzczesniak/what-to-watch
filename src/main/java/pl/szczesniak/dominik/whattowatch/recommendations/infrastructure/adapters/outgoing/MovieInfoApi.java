package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing;

import java.util.List;

public interface MovieInfoApi {

	TMDBMovieInfoResponse getPopularMovies();

	TMDBMovieGenreResponse getGenres();

	TMDBMovieInfoResponse getMoviesByGenre(List<Long> genreId);

}
