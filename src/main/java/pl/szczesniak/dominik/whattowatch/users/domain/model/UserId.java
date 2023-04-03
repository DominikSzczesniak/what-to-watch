package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class UserId {

    @Getter
    private final int value;

    public UserId(final int value) {
        this.value = value;
    }
}
