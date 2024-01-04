package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.MovieInfoApi;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class RecommendationService {

	private final RecommendationConfigurationManager configurationManager;
	private final MovieInfoApi movieInfoApi;
	private final RecommendedMoviesRepository repository;
	private final RecommendedMoviesFactory recommendedMoviesFactory;
	private final Clock clock;

	MovieInfoResponse recommendPopularMovies() {
		return movieInfoApi.getPopularMovies();
	}

	@Transactional
	public void recommendMoviesByConfiguration(final UserId userId) {
		if (!hasRecommendedMoviesForCurrentInterval(userId)) {
			final RecommendationConfiguration configuration = configurationManager.findBy(userId);
			final List<MovieInfo> recommendedFromApi = getMovieInfosByConfig(configuration.getGenres());
			final List<MovieInfo> latestRecommendedMovies = repository.findLatestRecommendedMovies(userId)
					.map(RecommendedMovies::getMovies)
					.orElse(Collections.emptyList());

			final RecommendedMovies recommendedMovies = recommendedMoviesFactory.createNewRecommendedMovies(
					configuration.getGenres(),
					recommendedFromApi,
					latestRecommendedMovies,
					userId
			);

			repository.create(recommendedMovies);
		}
	}

	RecommendedMovies getLatestRecommendedMovies(final UserId userId) {
		return repository.findLatestRecommendedMovies(userId)
				.orElseThrow(() -> new ObjectDoesNotExistException("No movies to recommend for user with id:" + userId.getValue()));
	}

	boolean hasRecommendedMoviesForCurrentInterval(final UserId userId) {
		final LocalDateTime now = LocalDateTime.now(clock);
		final LocalDateTime intervalStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.WEDNESDAY)).with(LocalTime.parse("00:00:00"));
		final LocalDateTime intervalEnd = now.with(TemporalAdjusters.next(DayOfWeek.TUESDAY)).with(LocalTime.parse("23:59:59"));

		return repository.existsByUserIdAndRecommendationDateBetween(userId, intervalStart, intervalEnd);
	}

	private List<MovieInfo> getMovieInfosByConfig(final Set<MovieGenre> genres) {
		Set<MovieGenre> movieGenres = genres;
		if (genres.size() == 0) {
			movieGenres = MovieGenre.allValues();
		}

		final List<Long> userGenresIds = movieInfoApi.mapGenreNamesToApiIds(movieGenres);
		final MovieInfoResponse moviesByGenre = movieInfoApi.getMoviesByGenre(userGenresIds);
		return moviesByGenre.getResults();
	}

}
