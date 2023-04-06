package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidUserIdValueException;

@EqualsAndHashCode
@ToString
public class UserId {

    @Getter
    private final int value;

    public UserId(final int value) {
        if (value < 1) {
            throw new InvalidUserIdValueException("UserId value must be higher than 0");
        }
        this.value = value;
    }
}
