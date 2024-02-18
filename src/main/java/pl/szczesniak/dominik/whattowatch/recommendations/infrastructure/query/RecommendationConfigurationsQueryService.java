package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.query;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.RecommendationConfigurationRequestResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class RecommendationConfigurationsQueryService implements RecommendationQueryService {

	private final JdbcTemplate jdbcTemplate;

	@Override
	public Optional<RecommendationConfigurationRequestResult> findRecommendationConfigurationQueryResultBy(final UserId userId) {
		final String sql = "SELECT r.user_id, r.id, c.genre " +
				"FROM recommendation_configuration r " +
				"JOIN configuration_genres c ON r.id = c.configuration_id " +
				"WHERE r.user_id = ?";

		return jdbcTemplate.query(sql, new Object[]{userId.getValue()}, (rs, rowNum) -> {
			final RecommendationConfigurationRequestResult result = new RecommendationConfigurationRequestResult(
					rs.getInt("user_id"),
					rs.getInt("id"),
					new HashSet<>()
			);
			fillGenres(rs, result.getGenres());
			return result;
		}).stream().findFirst();
	}

	private void fillGenres(final ResultSet rs, final Set<MovieGenre> genres) throws SQLException {
		if (rs.getString("genre") != null) {
			genres.add(MovieGenre.valueOf(rs.getString("genre")));
		}
	}


	@Override
	public List<UserId> findAllUsersWithRecommendationConfigurations() {
		final String sql = "SELECT DISTINCT user_id FROM recommendation_configuration";

		final List<Integer> userIds = jdbcTemplate.queryForList(sql, Integer.class);

		final List<UserId> result = new ArrayList<>();
		for (Integer userId : userIds) {
			result.add(new UserId(userId));
		}

		return result;
	}

}
