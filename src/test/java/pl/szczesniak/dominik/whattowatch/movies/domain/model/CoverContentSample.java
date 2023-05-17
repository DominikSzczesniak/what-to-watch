package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import org.apache.commons.lang3.RandomUtils;

public class CoverContentSample {

	public static byte[] createAnyCoverContent() {
		return RandomUtils.nextBytes(20);
	}

}
