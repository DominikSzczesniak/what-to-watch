package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.external;

import java.util.List;

public interface MovieInfoRepository {

	List<MovieInfo> findByGenreId(Long genreId);

}
