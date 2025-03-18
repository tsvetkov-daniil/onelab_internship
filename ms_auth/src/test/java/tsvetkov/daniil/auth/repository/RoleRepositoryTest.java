package tsvetkov.daniil.auth.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tsvetkov.daniil.auth.dto.Role;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Поиск существующей роли")
    void shouldFindRoleByName() {
        final String roleName = "USER";
        Role role = Role.builder()
                .name(roleName)
                .build();
        roleRepository.save(role);

        Optional<Role> foundRole = roleRepository.findByName(roleName);

        assertThat(foundRole).isPresent()
                .hasValueSatisfying(r -> assertThat(r.getName()).isEqualTo(roleName));
    }

    @Test
    @DisplayName("Поиск несуществующей роли")
    void shouldReturnEmptyWhenRoleNotFound() {
        Optional<Role> foundRole = roleRepository.findByName("ADMIN");

        assertThat(foundRole).isEmpty();
    }

    @Test
    @DisplayName("Сохранение роли без имени должно выбрасывать исключение")
    void shouldThrowExceptionWhenSavingRoleWithoutName() {
        Role role = Role.builder().build();

        assertThatThrownBy(() -> roleRepository.save(role))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
