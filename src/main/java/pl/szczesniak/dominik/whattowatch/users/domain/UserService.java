package pl.szczesniak.dominik.whattowatch.users.domain;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.szczesniak.dominik.whattowatch.commons.domain.DomainEventsPublisher;
import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.commands.CreateUser;
import pl.szczesniak.dominik.whattowatch.users.domain.model.events.UserDeleted;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidCredentialsException;

@RequiredArgsConstructor
@Slf4j
class UserService {

	private final UserRepository repository;

	private final UserRoleRepository roleRepository;

	private final PasswordEncoder passwordEncoder;

	private final DomainEventsPublisher domainEventsPublisher;

	@Transactional
	public UserId createUser(final CreateUser command) {
		final User user = createFrom(command);
		addDefaultRoleToUser(user);
		repository.create(user);
		return user.getUserId();
	}

	private User createFrom(final CreateUser command) {
		return new User(command.getUsername(), new UserPassword(passwordEncoder.encode(command.getUserPassword().getValue())));
	}

	private void addDefaultRoleToUser(final User user) {
		final UserRole userRole = roleRepository.findBy(RoleName.USER).get();
		user.addRole(userRole);
	}

	UserId login(final Username username, final UserPassword userPassword) {
		return repository.findBy(username)
				.filter(user -> passwordEncoder.matches(userPassword.getValue(), user.getUserPassword().getValue()))
				.orElseThrow(() -> new InvalidCredentialsException("Invalid credentials, could not log in.")).getUserId();
	}

	void deleteUser(final UserId userId) {
		repository.deleteUser(userId);
		domainEventsPublisher.publish(new UserDeleted(userId));
	}

}
