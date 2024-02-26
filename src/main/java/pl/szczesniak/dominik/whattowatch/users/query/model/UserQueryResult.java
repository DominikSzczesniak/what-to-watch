package pl.szczesniak.dominik.whattowatch.users.query.model;

import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;

import java.util.List;

@Value
public class UserQueryResult {

	@NonNull Integer id;

	@NonNull String username;

	@NonNull String userPassword;

	@NonNull List<RoleName> roles;

}
