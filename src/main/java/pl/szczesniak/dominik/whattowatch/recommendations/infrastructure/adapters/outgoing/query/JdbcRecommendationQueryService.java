package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.query;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoApis;
import pl.szczesniak.dominik.whattowatch.recommendations.query.RecommendationConfigurationQueryService;
import pl.szczesniak.dominik.whattowatch.recommendations.query.RecommendedMoviesQueryService;
import pl.szczesniak.dominik.whattowatch.recommendations.query.model.RecommendationConfigurationRequestResult;
import pl.szczesniak.dominik.whattowatch.recommendations.query.model.RecommendedMoviesQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JdbcRecommendationQueryService implements RecommendationConfigurationQueryService, RecommendedMoviesQueryService {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public Optional<RecommendationConfigurationRequestResult> findRecommendationConfigurationQueryResultBy(final UserId userId) {
		final String sql = "SELECT r.user_id, r.id, c.genre " +
				"FROM recommendation_configuration r " +
				"JOIN configuration_genres c ON r.id = c.configuration_id " +
				"WHERE r.user_id = :userId";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId.getValue());

		final List<RecommendationConfigurationRequestResult> results = namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			RecommendationConfigurationRequestResult result = new RecommendationConfigurationRequestResult(
					rs.getInt("user_id"),
					rs.getInt("id"),
					new HashSet<>()
			);
			fillGenres(rs, result.getGenres());
			return result;
		});

		return results.stream().findFirst();
	}

	private void fillGenres(final ResultSet rs, final Set<MovieGenre> genres) throws SQLException {
		if (rs.getString("genre") != null) {
			genres.add(MovieGenre.valueOf(rs.getString("genre")));
		}
	}


	@Override
	public List<UserId> findAllUsersWithRecommendationConfigurations() {
		final String sql = "SELECT DISTINCT user_id " +
				"FROM recommendation_configuration";

		final List<Integer> userIds = namedParameterJdbcTemplate.queryForList(sql, Collections.emptyMap(), Integer.class);

		final List<UserId> result = new ArrayList<>();
		for (Integer userId : userIds) {
			result.add(new UserId(userId));
		}

		return result;
	}

	@Override
	public Optional<RecommendedMoviesQueryResult> findLatestRecommendedMoviesQueryResult(final UserId userId) {
		final String sql = "SELECT rm.id, rm.creation_date, rm.end_interval, " +
				"mi.movie_info_id, mi.overview, mi.title, mi.external_id, mi.external_api " +
				"FROM recommended_movies rm " +
				"JOIN movie_info mi ON rm.id = mi.id " +
				"WHERE rm.user_id = :userId " +
				"ORDER BY rm.creation_date DESC " +
				"LIMIT 2";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId.getValue());

		return namedParameterJdbcTemplate.query(sql, params, rs -> {
			if (rs.next()) {
				int recommendedMoviesId = rs.getInt("id");
				LocalDateTime creationDate = rs.getTimestamp("creation_date").toLocalDateTime();
				LocalDateTime endInterval = rs.getTimestamp("end_interval").toLocalDateTime();

				List<MovieInfo> movieInfoList = new ArrayList<>();
				do {
					int movieInfoId = rs.getInt("movie_info_id");
					String overview = rs.getString("overview");
					String title = rs.getString("title");
					int externalId = rs.getInt("external_id");
					MovieInfoApis externalApi = MovieInfoApis.valueOf(rs.getString("external_api"));

					List<MovieGenre> genres = fetchGenres(movieInfoId);

					MovieInfo movieInfo = new MovieInfo(genres, overview, title, externalId, externalApi);
					movieInfoList.add(movieInfo);
				} while (rs.next());

				return Optional.of(new RecommendedMoviesQueryResult(recommendedMoviesId, movieInfoList, creationDate, endInterval));
			} else {
				return Optional.empty();
			}
		});
	}

	private List<MovieGenre> fetchGenres(int movieInfoId) {
		final String sqlGenres = "SELECT genres " +
				"FROM movie_info_genres " +
				"WHERE movie_info_movie_info_id = :movieInfoId";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("movieInfoId", movieInfoId);

		return namedParameterJdbcTemplate.queryForList(sqlGenres, params, String.class)
				.stream()
				.map(MovieGenre::valueOf)
				.collect(Collectors.toList());
	}

}
