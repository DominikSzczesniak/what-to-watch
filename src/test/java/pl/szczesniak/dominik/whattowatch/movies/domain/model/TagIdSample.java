package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import java.util.UUID;

public class TagIdSample {

	public static TagId createAnyTagId() {
		return new TagId(UUID.randomUUID());
	}

}
