package pl.szczesniak.dominik.whattowatch.users.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.BaseEntity;
import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;

@Entity
@Table
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UserRole extends BaseEntity {

	@Column(unique = true)
	@Enumerated(value = EnumType.STRING)
	private RoleName roleName;

	public UserRole(@NonNull final RoleName roleName) {
		this.roleName = roleName;
	}

}
