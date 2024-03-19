package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.query;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoApis;
import pl.szczesniak.dominik.whattowatch.recommendations.query.RecommendedMoviesQueryService;
import pl.szczesniak.dominik.whattowatch.recommendations.query.model.RecommendedMoviesQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JdbcRecommendedMoviesQueryService implements RecommendedMoviesQueryService {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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
				final int recommendedMoviesId = rs.getInt("id");
				final LocalDateTime creationDate = rs.getTimestamp("creation_date").toLocalDateTime();
				final LocalDateTime endInterval = rs.getTimestamp("end_interval").toLocalDateTime();

				final List<MovieInfo> movieInfoList = new ArrayList<>();
				do {
					final int movieInfoId = rs.getInt("movie_info_id");
					final String overview = rs.getString("overview");
					final String title = rs.getString("title");
					final int externalId = rs.getInt("external_id");
					final MovieInfoApis externalApi = MovieInfoApis.valueOf(rs.getString("external_api"));

					final List<MovieGenre> genres = fetchGenres(movieInfoId);

					final MovieInfo movieInfo = new MovieInfo(genres, overview, title, externalId, externalApi);
					movieInfoList.add(movieInfo);
				} while (rs.next());

				return Optional.of(new RecommendedMoviesQueryResult(recommendedMoviesId, movieInfoList, creationDate, endInterval));
			}

			return Optional.empty();
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
