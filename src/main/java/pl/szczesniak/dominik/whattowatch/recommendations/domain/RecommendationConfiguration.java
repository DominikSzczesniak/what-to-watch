package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.GenreId;

import java.util.List;
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"configurationId"})
public class RecommendationConfiguration {

	private ConfigurationId configurationId;

	private List<GenreId> genres;

}
