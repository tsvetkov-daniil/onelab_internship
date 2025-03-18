package tsvetkov.daniil.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.auth.dto.Role;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
class RoleServiceTest {

    private final RoleService roleService;

    @Autowired
    RoleServiceTest(RoleService roleService) {
        this.roleService = roleService;
    }

    @BeforeEach
    void setUp() {
        roleService.deleteAll();
    }

    @Test
    @DisplayName("Сохранение роли")
    void testSaveRole() {
        Role role = Role.builder().name("admin").build();
        Role savedRole = roleService.save(role);

        assertThat(savedRole).isNotNull();
        assertThat(savedRole.getName()).isEqualTo("admin");
    }

    @Test
    @DisplayName("Поиск роли по id")
    void testFindById() {
        Role role = roleService.save(Role.builder().name("moderator").build());
        Optional<Role> foundRole = roleService.findById(role.getId());
        assertThat(foundRole).isPresent().contains(role);
    }

    @Test
    @DisplayName("Поиск роли по имени")
    void testFindByName() {
        Role role = roleService.save(Role.builder().name("user").build());
        Optional<Role> foundRole = roleService.findByName("user");
        assertThat(foundRole).isPresent().contains(role);
    }

    @Test
    @DisplayName("Поиск всех")
    void testFindAll() {
        roleService.save(Role.builder().name("admin").build());
        roleService.save(Role.builder().name("user").build());

        Set<Role> roles = roleService.findAll(0, 10);
        assertThat(roles).hasSize(2);
    }

    @Test
    @DisplayName("Удаление роли по id")
    void testDeleteById() {
        Role role = roleService.save(Role.builder().name("temp").build());
        roleService.deleteById(role.getId());
        Optional<Role> foundRole = roleService.findById(role.getId());
        assertThat(foundRole).isNotPresent();
    }

    @Test
    @DisplayName("Поиск дефолтной роли")
    void testGetDefaultRole() {
        Optional<Role> defaultRole = roleService.getDefaultRole();
        assertThat(defaultRole).isPresent();
        assertThat(defaultRole.get().getName()).isEqualTo("user");
    }
}
