package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class CreateUser {

	@NonNull Username username;

	@NonNull UserPassword userPassword;

}
