package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = {"commentId"})
public class MovieComment {

	@Id
	private UUID commentId;

	private Integer movieId;

	@Column(name = "comment_value")
	private String value;

}
