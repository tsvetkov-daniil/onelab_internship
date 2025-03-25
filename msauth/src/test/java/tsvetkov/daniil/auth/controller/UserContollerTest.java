package tsvetkov.daniil.auth.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.auth.entity.Reader;
import tsvetkov.daniil.auth.entity.Role;
import tsvetkov.daniil.auth.repository.ReaderRepository;
import tsvetkov.daniil.auth.repository.RoleRepository;

import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class UserContollerTest {

    private final TestRestTemplate restTemplate;
    private final ReaderRepository readerRepository;
    private final RoleRepository roleRepository;

    @Autowired
    UserContollerTest(TestRestTemplate restTemplate, ReaderRepository readerRepository, RoleRepository roleRepository) {
        this.restTemplate = restTemplate;
        this.readerRepository = readerRepository;
        this.roleRepository = roleRepository;
    }

    private Reader createValidReader() {
        return Reader.builder()
                .firstName("Иван")
                .lastName("Иванов")
                .username("ivan123")
                .email("ivan@example.com")
                .build();
    }

    private Role createValidRole() {
        return Role.builder()
                .name("test_role")
                .build();
    }

    @BeforeEach
    void setUp() {
        readerRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @DisplayName("Успешное создание нового пользователя")
    void testCreateReader() {
        Reader reader = createValidReader();
        ResponseEntity<Reader> response = restTemplate.postForEntity("/api/v1/readers", reader, Reader.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getFirstName()).isEqualTo("Иван");
        Assertions.assertThat(response.getBody().getRoles()).isNotEmpty();
    }

    @Test
    @DisplayName("Получение пользователя по ID")
    void testGetReaderById() {
        Reader reader = createValidReader();
        ResponseEntity<Reader> createResponse = restTemplate.postForEntity("/api/v1/readers", reader, Reader.class);

        Long id = createResponse.getBody().getId();
        ResponseEntity<Reader> response = restTemplate.getForEntity("/api/v1/readers/{id}", Reader.class, id);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getId()).isEqualTo(id);
        Assertions.assertThat(response.getBody().getEmail()).isEqualTo("ivan@example.com");
    }

    @Test
    @DisplayName("Получение несуществующего пользователя по ID")
    void testGetReaderByIdNotFound() {
        ResponseEntity<Reader> response = restTemplate.getForEntity("/api/v1/readers/999", Reader.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Получение пользователя по email")
    void testGetReaderByEmail() {
        Reader reader = createValidReader();
        restTemplate.postForEntity("/api/v1/readers", reader, Reader.class);

        ResponseEntity<Reader> response = restTemplate.getForEntity("/api/v1/readers/email/{email}", Reader.class, "ivan@example.com");

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getEmail()).isEqualTo("ivan@example.com");
    }

    @Test
    @DisplayName("Получение всех пользователей с пагинацией")
    void testGetAllReaders() {
        restTemplate.postForEntity("/api/v1/readers", createValidReader(), Reader.class);
        restTemplate.postForEntity("/api/v1/readers", Reader.builder()
                .firstName("Пётр")
                .lastName("Петров")
                .username("petr456")
                .email("petr@example.com")
                .build(), Reader.class);

        ResponseEntity<Set> response = restTemplate.getForEntity("/api/v1/readers?pageNumber=0&pageSize=2", Set.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).hasSize(2);
    }

    @Test
    @DisplayName("Обновление пользователя")
    void testUpdateReader() {
        Reader reader = createValidReader();
        ResponseEntity<Reader> createResponse = restTemplate.postForEntity("/api/v1/readers", reader, Reader.class);
        Long id = createResponse.getBody().getId();

        Reader updatedReader = createValidReader();
        updatedReader.setLastName("Сидоров");
        HttpEntity<Reader> request = new HttpEntity<>(updatedReader);
        ResponseEntity<Reader> response = restTemplate.exchange("/api/v1/readers/{id}", HttpMethod.PUT, request, Reader.class, id);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getLastName()).isEqualTo("Сидоров");
        Assertions.assertThat(response.getBody().getId()).isEqualTo(id); // ID должен остаться тем же
    }

    @Test
    @DisplayName("Удаление пользователя")
    void testDeleteReader() {
        Reader reader = createValidReader();
        ResponseEntity<Reader> createResponse = restTemplate.postForEntity("/api/v1/readers", reader, Reader.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Void> response = restTemplate.exchange("/api/v1/readers/{id}", HttpMethod.DELETE, null, Void.class, id);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Успешное создание новой роли")
    void testCreateRole() {
        Role role = createValidRole();
        ResponseEntity<Role> response = restTemplate.postForEntity("/api/v1/roles", role, Role.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo("test_role");
    }

    @Test
    @DisplayName("Получение роли по ID")
    void testGetRoleById() {
        Role role = createValidRole();
        ResponseEntity<Role> createResponse = restTemplate.postForEntity("/api/v1/roles", role, Role.class);

        Long id = createResponse.getBody().getId();
        ResponseEntity<Role> response = restTemplate.getForEntity("/api/v1/roles/{id}", Role.class, id);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getId()).isEqualTo(id);
        Assertions.assertThat(response.getBody().getName()).isEqualTo("test_role");
    }

    @Test
    @DisplayName("Получение роли по имени")
    void testGetRoleByName() {
        Role role = createValidRole();
        restTemplate.postForEntity("/api/v1/roles", role, Role.class);

        ResponseEntity<Role> response = restTemplate.getForEntity("/api/v1/roles/name/{name}", Role.class, "test_role");

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getName()).isEqualTo("test_role");
    }

    @Test
    @DisplayName("Получение всех ролей с пагинацией")
    void testGetAllRoles() {
        restTemplate.postForEntity("/api/v1/roles", createValidRole(), Role.class);
        restTemplate.postForEntity("/api/v1/roles", Role.builder().name("another_role").build(), Role.class);

        ResponseEntity<Set> response = restTemplate.getForEntity("/api/v1/roles?pageNumber=0&pageSize=1", Set.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).hasSize(1);
    }

    @Test
    @DisplayName("Удаление роли")
    void testDeleteRole() {
        Role role = createValidRole();
        ResponseEntity<Role> createResponse = restTemplate.postForEntity("/api/v1/roles", role, Role.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Void> response = restTemplate.exchange("/api/v1/roles/{id}", HttpMethod.DELETE, null, Void.class, id);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<Role> getResponse = restTemplate.getForEntity("/api/v1/roles/{id}", Role.class, id);
        Assertions.assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Удаление всех ролей")
    void testDeleteAllRoles() {
        restTemplate.postForEntity("/api/v1/roles", createValidRole(), Role.class);
        restTemplate.postForEntity("/api/v1/roles", Role.builder().name("another_role").build(), Role.class);

        ResponseEntity<Void> response = restTemplate.exchange("/api/v1/roles", HttpMethod.DELETE, null, Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<Set> getResponse = restTemplate.getForEntity("/api/v1/roles", Set.class);
        Assertions.assertThat(getResponse.getBody()).isEmpty();
    }

    @Test
    @DisplayName("Получение дефолтной роли")
    void testGetDefaultRole() {
        ResponseEntity<Role> response = restTemplate.getForEntity("/api/v1/roles/default", Role.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getName()).isEqualTo("user");
    }
}