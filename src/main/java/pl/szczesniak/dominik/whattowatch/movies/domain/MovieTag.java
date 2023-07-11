package pl.szczesniak.dominik.whattowatch.movies.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagLabel;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Set;

@Entity
@Table(name = "tag_table", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"user_id_label", "tag_label"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(of = {"tagId"})
public class MovieTag {

	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "tag_id"))
	private TagId tagId;

	@AttributeOverride(name = "value", column = @Column(name = "tag_label"))
	private TagLabel label;

	@AttributeOverride(name = "value", column = @Column(name = "user_id_label"))
	private UserId userId;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "tags")
	@JsonIgnoreProperties("tags")
	private Set<Movie> movies;

	MovieTag(final TagId tagId, final TagLabel label, final UserId userId) {
		this.tagId = tagId;
		this.label = label;
		this.userId = userId;
	}

}
