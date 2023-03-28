package pl.szczesniak.dominik.whattowatch.users.domain.model;


public class UserService{

    private final UserRepository repository;

    public UserService(final UserRepository repository) {
        this.repository = repository;
    }

    public int getUserId(final String username) {
        return repository.getUserId(username);
    }

    public User createUser(String username) {
        return new User(username);
    }

//    public User getUser(String username) {
//        return new User("Grzegorz");
//    }

}
