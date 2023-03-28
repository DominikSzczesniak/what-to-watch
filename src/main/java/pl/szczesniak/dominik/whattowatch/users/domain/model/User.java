package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class User {

    @Getter
    private final UserId id;
    @Getter
    private final String userName;

    public User(final String userName) {
        this.userName = userName;
        id = new UserId();
    }
}
