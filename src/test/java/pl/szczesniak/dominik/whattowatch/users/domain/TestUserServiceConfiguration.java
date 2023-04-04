package pl.szczesniak.dominik.whattowatch.users.domain;

public class TestUserServiceConfiguration {

    static UserService userService(final UserRepository userRepository) {
        return new UserService(userRepository);
    }
}


