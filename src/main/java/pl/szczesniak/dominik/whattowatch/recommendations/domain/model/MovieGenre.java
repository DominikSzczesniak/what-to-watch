package pl.szczesniak.dominik.whattowatch.recommendations.domain.model;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public enum MovieGenre {
	ACTION,
	ADVENTURE,
	ANIMATION,
	COMEDY,
	CRIME,
	DOCUMENTARY,
	DRAMA,
	FAMILY,
	FANTASY,
	HISTORY,
	HORROR,
	MUSIC,
	MYSTERY,
	ROMANCE,
	SCIENCE_FICTION,
	TV_MOVIE,
	THRILLER,
	WAR,
	WESTERN;

	public static Set<MovieGenre> allValues() {
		return ImmutableSet.copyOf(MovieGenre.values());
	}

}
