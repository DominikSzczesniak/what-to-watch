package pl.szczesniak.dominik.whattowatch.users.domain.model;

import org.apache.commons.lang3.RandomStringUtils;

public class UserPasswordSample {

	public static UserPassword createAnyUserPassword() {
		return new UserPassword(RandomStringUtils.randomAlphabetic(5));
	}

}
