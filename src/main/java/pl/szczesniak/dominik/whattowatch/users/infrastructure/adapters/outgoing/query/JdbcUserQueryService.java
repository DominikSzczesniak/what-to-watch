package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.outgoing.query;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.query.UserQueryService;
import pl.szczesniak.dominik.whattowatch.users.query.model.UserQueryResult;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JdbcUserQueryService implements UserQueryService {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public boolean isUsernameTaken(final Username username) {
		final String sql = "SELECT EXISTS (SELECT 1 FROM app_user WHERE user_name = :username)";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("username", username.getValue());

		return jdbcTemplate.queryForObject(sql, params, Boolean.class);
	}

	@Override
	public boolean exists(final UserId userId) {
		final String sql = "SELECT COUNT(*) FROM app_user WHERE id = :userId";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId.getValue());

		final Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);

		return count != null && count > 0;
	}

	@Override
	public Optional<UserQueryResult> findUserQueryResult(final Username username) {
		final String sql = "SELECT u.user_name, u.password, u.id, ur.role_name " +
				"FROM app_user u " +
				"JOIN app_user_roles r ON u.id = r.user_id " +
				"JOIN user_role ur ON r.roles_id = ur.id " +
				"WHERE u.user_name = :username";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("username", username.getValue());

		return jdbcTemplate.query(sql, params, rs -> {
			if (rs.next()) {
				final UserQueryResult result = new UserQueryResult(
						rs.getInt("id"),
						rs.getString("user_name"),
						rs.getString("password"),
						new ArrayList<>()
				);

				fillRoles(rs, result.getRoles());

				return Optional.of(result);
			}

			return Optional.empty();
		});
	}

	private static void fillRoles(final ResultSet rs, final List<RoleName> roleNames) throws SQLException {
		if (rs.getString("role_name") != null) {
			roleNames.add(
					RoleName.valueOf(rs.getString("role_name"))
			);
		}
	}

}
