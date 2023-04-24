package pl.szczesniak.dominik.whattowatch.users.domain.model;

import org.apache.commons.lang3.RandomStringUtils;

public class UsernameSample {

	public static Username createAnyUsername() {
		return new Username(RandomStringUtils.randomAlphabetic(5));
	}

}
