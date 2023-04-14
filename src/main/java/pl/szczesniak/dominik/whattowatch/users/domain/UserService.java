package pl.szczesniak.dominik.whattowatch.users.domain;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

@RequiredArgsConstructor
public class UserService {

	private final UserRepository repository;

	public UserId createUser(final String username) {
		User user = new User(new Username(username), repository.nextUserId());
		repository.create(user);
		return user.getUserId();
	}

	public boolean exists(final UserId userId) {
		return repository.exists(userId);
	}


	public UserId login(String username) {
		return repository.findBy(username).get().getUserId();
	}

}
