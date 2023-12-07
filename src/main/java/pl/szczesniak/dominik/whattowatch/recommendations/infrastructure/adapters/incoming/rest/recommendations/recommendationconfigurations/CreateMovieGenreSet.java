package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.recommendationconfigurations;

import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class CreateMovieGenreSet {

	static Set<MovieGenre> createGenreSet(final List<String> genres) {
		if (genres.get(0).equalsIgnoreCase("all")) {
			return EnumSet.allOf(MovieGenre.class);
		} else {
			return genres.stream()
					.map(MovieGenre::valueOf)
					.collect(Collectors.toSet());
		}
	}

}
