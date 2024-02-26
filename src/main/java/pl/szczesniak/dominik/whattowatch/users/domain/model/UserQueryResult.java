package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class UserQueryResult {

	@NonNull Integer id;

	@NonNull String username;

	@NonNull String userPassword;

	@NonNull List<RoleName> roles;

}
