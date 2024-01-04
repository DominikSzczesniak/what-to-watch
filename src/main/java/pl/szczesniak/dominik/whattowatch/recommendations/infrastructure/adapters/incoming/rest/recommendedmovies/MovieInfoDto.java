package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendedmovies;

import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Value
class MovieInfoDto {

	@NonNull String title;
	@NonNull String overview;
	@NonNull List<String> genresNames;
	@NonNull Integer externalId;
	@NonNull String externalApi;

	static List<MovieInfoDto> mapToMovieInfoDto(final List<MovieInfo> movies) {
		final List<MovieInfoDto> movieDtos = new ArrayList<>();
		for (MovieInfo movieInfo : movies) {
			final List<String> genres = movieInfo.getGenres().stream()
					.map(Enum::name)
					.collect(Collectors.toList());
			movieDtos.add(new MovieInfoDto(
					movieInfo.getTitle(),
					movieInfo.getOverview(),
					genres,
					movieInfo.getExternalId(),
					movieInfo.getExternalApi().toString()));
		}
		return movieDtos;
	}

}
