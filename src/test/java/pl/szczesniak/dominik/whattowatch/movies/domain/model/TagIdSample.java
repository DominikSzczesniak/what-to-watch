package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import java.util.UUID;

public class TagIdSample {

	public static MovieTagId createAnyTagId() {
		return new MovieTagId(UUID.randomUUID());
	}

}
