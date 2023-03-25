package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.Getter;

public class User {

    @Getter
    private final UserId id;
    @Getter
    private final String login;

    public User(final String name) {
        this.login = name;
        id = new UserId();
    }
}
