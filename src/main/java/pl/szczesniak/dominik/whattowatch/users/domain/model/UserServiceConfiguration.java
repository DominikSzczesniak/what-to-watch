package pl.szczesniak.dominik.whattowatch.users.domain.model;

public class UserServiceConfiguration {

    public UserService userService(final UserRepository userRepository) {
        return new UserService(userRepository);
    }
}
