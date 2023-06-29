package pl.szczesniak.dominik.whattowatch.users.domain.model;

import java.util.Random;

public class UserIdSample {

	public static UserId createAnyUserId() {
		return new UserId(new Random().nextInt(1, 99999));
	}

}
