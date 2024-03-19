package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing;

import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenreResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;

import java.util.Set;

public interface MovieInfoApi {

	MovieInfoResponse getPopularMovies();

	MovieGenreResponse getGenres();

	MovieInfoResponse getMoviesByGenre(Set<MovieGenre> genres);

}
