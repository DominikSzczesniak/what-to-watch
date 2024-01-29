package pl.szczesniak.dominik.whattowatch.users.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.BaseEntity;
import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"role_name"})
})
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRole extends BaseEntity {

	@AttributeOverride(name = "value", column = @Column(name = "role_name"))
	private RoleName roleName;

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "roles")
	@JsonIgnoreProperties("roles")
	private List<User> users;

	public UserRole(@NonNull final RoleName roleName) {
		this.roleName = roleName;
		users = new ArrayList<>();
	}

}
