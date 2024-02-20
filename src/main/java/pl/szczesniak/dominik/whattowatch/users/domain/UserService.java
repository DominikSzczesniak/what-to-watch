package pl.szczesniak.dominik.whattowatch.users.domain;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.commands.CreateUser;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidCredentialsException;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository repository;

	private final UserRoleRepository roleRepository;

	private final PasswordEncoder passwordEncoder;

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

	public boolean exists(final UserId userId) {
		return repository.exists(userId);
	}

	public UserId login(final Username username, final UserPassword userPassword) {
		return repository.findBy(username)
				.filter(user -> passwordEncoder.matches(userPassword.getValue(), user.getUserPassword().getValue()))
				.orElseThrow(() -> new InvalidCredentialsException("Invalid credentials, could not log in.")).getUserId();
	}

	public boolean isUsernameTaken(final Username username) {
		return repository.findBy(username).isPresent();
	}

	public Optional<User> findUserBy(final Username username) {
		return repository.findBy(username);
	}

}
