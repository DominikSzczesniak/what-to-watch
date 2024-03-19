package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.apache.commons.lang3.RandomStringUtils;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenreResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfo;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoApis;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieInfoResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.MovieInfoApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class InMemoryMovieInfoApiRepository implements MovieInfoApi {

	private final Map<Integer, MovieInfo> movies = new HashMap<>();

	private final Map<Long, MovieGenre> assignedGenreIds = new HashMap<>();

	InMemoryMovieInfoApiRepository() {
		addGenres();
		addMovieInfos();
	}

	@Override
	public MovieInfoResponse getPopularMovies() {
		return new MovieInfoResponse(List.of(movies.get(1), movies.get(2), movies.get(3), movies.get(4)));
	}

	@Override
	public MovieGenreResponse getGenres() {
		return new MovieGenreResponse(assignedGenreIds);
	}

	@Override
	public MovieInfoResponse getMoviesByGenre(final Set<MovieGenre> genres) {
		final List<Long> genreIds = mapGenreNamesToApiIds(genres);
		final List<MovieInfo> moviesByGenres = movies.values().stream()
				.filter(movieInfo -> movieInfo.getGenres().stream()
						.anyMatch(movieGenre -> genreIds.stream()
								.map(assignedGenreIds::get)
								.anyMatch(genre -> genre == movieGenre)))
				.toList();

		return new MovieInfoResponse(moviesByGenres);
	}

	private List<Long> mapGenreNamesToApiIds(final Set<MovieGenre> genres) {
		return genres.stream()
				.filter(assignedGenreIds::containsValue)
				.map(genre -> assignedGenreIds.entrySet().stream()
						.filter(apiGenreEntry -> apiGenreEntry.getValue().equals(genre))
						.findFirst()
						.map(Map.Entry::getKey)
						.orElse(null))
				.collect(Collectors.toList());
	}

	private void addGenres() {
		assignedGenreIds.put(28L, MovieGenre.ACTION);
		assignedGenreIds.put(12L, MovieGenre.ADVENTURE);
		assignedGenreIds.put(16L, MovieGenre.ANIMATION);
		assignedGenreIds.put(35L, MovieGenre.COMEDY);
		assignedGenreIds.put(80L, MovieGenre.CRIME);
		assignedGenreIds.put(99L, MovieGenre.DOCUMENTARY);
		assignedGenreIds.put(18L, MovieGenre.DRAMA);
		assignedGenreIds.put(10751L, MovieGenre.FAMILY);
		assignedGenreIds.put(14L, MovieGenre.FANTASY);
		assignedGenreIds.put(36L, MovieGenre.HISTORY);
		assignedGenreIds.put(27L, MovieGenre.HORROR);
		assignedGenreIds.put(10402L, MovieGenre.MUSIC);
		assignedGenreIds.put(9648L, MovieGenre.MYSTERY);
		assignedGenreIds.put(10749L, MovieGenre.ROMANCE);
		assignedGenreIds.put(878L, MovieGenre.SCIENCE_FICTION);
		assignedGenreIds.put(10770L, MovieGenre.TV_MOVIE);
		assignedGenreIds.put(53L, MovieGenre.THRILLER);
		assignedGenreIds.put(10752L, MovieGenre.WAR);
		assignedGenreIds.put(37L, MovieGenre.WESTERN);
	}

	private void addMovieInfos() {
		final AtomicInteger nextId = new AtomicInteger(1);
		movies.put(1, new MovieInfo(List.of(MovieGenre.ACTION, MovieGenre.TV_MOVIE), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(2, new MovieInfo(List.of(MovieGenre.ACTION, MovieGenre.TV_MOVIE, MovieGenre.WESTERN), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(3, new MovieInfo(List.of(MovieGenre.CRIME), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(4, new MovieInfo(List.of(MovieGenre.COMEDY), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(5, new MovieInfo(List.of(MovieGenre.FAMILY), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(6, new MovieInfo(List.of(MovieGenre.FANTASY), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(7, new MovieInfo(List.of(MovieGenre.ACTION, MovieGenre.SCIENCE_FICTION, MovieGenre.FANTASY), generateRandomString(), generateRandomString(), 7, MovieInfoApis.TMDB));
		movies.put(8, new MovieInfo(List.of(MovieGenre.ADVENTURE, MovieGenre.FANTASY), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(9, new MovieInfo(List.of(MovieGenre.HORROR, MovieGenre.MYSTERY), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(10, new MovieInfo(List.of(MovieGenre.DRAMA, MovieGenre.ROMANCE), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(11, new MovieInfo(List.of(MovieGenre.COMEDY, MovieGenre.FAMILY), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(12, new MovieInfo(List.of(MovieGenre.WESTERN), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(13, new MovieInfo(List.of(MovieGenre.THRILLER, MovieGenre.CRIME), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(14, new MovieInfo(List.of(MovieGenre.DOCUMENTARY), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(15, new MovieInfo(List.of(MovieGenre.ADVENTURE, MovieGenre.HISTORY), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(16, new MovieInfo(List.of(MovieGenre.SCIENCE_FICTION, MovieGenre.ACTION), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(17, new MovieInfo(List.of(MovieGenre.HISTORY, MovieGenre.WAR), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(18, new MovieInfo(List.of(MovieGenre.ANIMATION, MovieGenre.FAMILY), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(19, new MovieInfo(List.of(MovieGenre.TV_MOVIE, MovieGenre.CRIME, MovieGenre.ACTION), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(20, new MovieInfo(List.of(MovieGenre.THRILLER, MovieGenre.MYSTERY), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(21, new MovieInfo(List.of(MovieGenre.FANTASY, MovieGenre.ADVENTURE), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(22, new MovieInfo(List.of(MovieGenre.COMEDY), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(23, new MovieInfo(List.of(MovieGenre.DOCUMENTARY, MovieGenre.MUSIC), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(24, new MovieInfo(List.of(MovieGenre.DRAMA), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(25, new MovieInfo(List.of(MovieGenre.HORROR, MovieGenre.THRILLER), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(26, new MovieInfo(List.of(MovieGenre.SCIENCE_FICTION, MovieGenre.ACTION), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(27, new MovieInfo(List.of(MovieGenre.ADVENTURE, MovieGenre.COMEDY), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(28, new MovieInfo(List.of(MovieGenre.FANTASY, MovieGenre.FAMILY), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(29, new MovieInfo(List.of(MovieGenre.THRILLER, MovieGenre.CRIME), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(30, new MovieInfo(List.of(MovieGenre.DRAMA), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(31, new MovieInfo(List.of(MovieGenre.COMEDY), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(32, new MovieInfo(List.of(MovieGenre.HISTORY, MovieGenre.WAR), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(33, new MovieInfo(List.of(MovieGenre.HORROR, MovieGenre.MYSTERY), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(34, new MovieInfo(List.of(MovieGenre.SCIENCE_FICTION, MovieGenre.FANTASY), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
		movies.put(35, new MovieInfo(List.of(MovieGenre.ACTION, MovieGenre.THRILLER), generateRandomString(), generateRandomString(), nextId.incrementAndGet(), MovieInfoApis.TMDB));
	}

	private static String generateRandomString() {
		return RandomStringUtils.randomAlphabetic(10);
	}

}
