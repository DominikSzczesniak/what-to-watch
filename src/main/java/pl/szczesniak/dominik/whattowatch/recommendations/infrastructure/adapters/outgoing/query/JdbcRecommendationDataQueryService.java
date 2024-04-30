package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.query;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.query.RecommendationDataQueryService;
import pl.szczesniak.dominik.whattowatch.recommendations.query.model.RecommendationDataQueryResult;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class JdbcRecommendationDataQueryService implements RecommendationDataQueryService {

	private final JdbcTemplate jdbcTemplate;

	@Override
	public List<RecommendationDataQueryResult> findAllRecommendationDataQueryResult() {
		String sql = "SELECT rc.user_id, cg.genre " +
				"FROM recommendation_configuration rc " +
				"JOIN configuration_genres cg ON rc.id = cg.configuration_id";

		return jdbcTemplate.query(sql, (resultSet, i) -> {
			Integer userId = resultSet.getInt("user_id");
			MovieGenre genre = MovieGenre.valueOf(resultSet.getString("genre"));
			return new RecommendationDataQueryResult(userId, Set.of(genre));
		});
	}

}
