package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenreResponse;
import pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.MovieInfoApi;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//class GenreServiceTest {
//
//	private GenreService tut;
//	private MovieInfoApi movieInfoApi;
//
//	@BeforeEach
//	void setUp() {
//		movieInfoApi = mock(MovieInfoApi.class);
//		tut = new GenreService(movieInfoApi);
//	}
//
//	@Test
//	void name() {
//		// given
//		when(movieInfoApi.getGenres()).thenReturn(new MovieGenreResponse(Map.of(12L, MovieGenre.ADVENTURE)));
//
//		// when
//		List<Long> genreIdsForGenres = tut.getGenreIdsForGenres(Set.of(MovieGenre.ADVENTURE));
//
//		// then
//		assertThat(genreIdsForGenres.get(0)).isEqualTo(12L);
//	}
//}