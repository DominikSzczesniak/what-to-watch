package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing;

import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenreResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;

import java.util.List;
import java.util.Set;

public interface MovieInfoApi {

	MovieInfoResponse getPopularMovies();

	MovieGenreResponse getGenres();

	MovieInfoResponse getMoviesByGenre(List<Long> genreId);

	List<Long> mapGenreNamesToApiIds(Set<MovieGenre> genres);

}
