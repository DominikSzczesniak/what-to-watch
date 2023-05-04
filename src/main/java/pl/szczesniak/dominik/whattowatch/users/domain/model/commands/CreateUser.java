package pl.szczesniak.dominik.whattowatch.users.domain.model.commands;

import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

@Value
public class CreateUser {

	@NonNull Username username;

	@NonNull UserPassword userPassword;

}
