package tsvetkov.daniil.auth.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tsvetkov.daniil.auth.entity.Reader;
import tsvetkov.daniil.auth.entity.Role;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.*;


@DataJpaTest
class ReaderRepositoryTest {

    private final ReaderRepository readerRepository;
    private final RoleRepository roleRepository;

    @Autowired
    ReaderRepositoryTest(ReaderRepository readerRepository, RoleRepository roleRepository) {
        this.readerRepository = readerRepository;
        this.roleRepository = roleRepository;
    }


    @Test
    @DisplayName("Поиск существующего email")
    void testFindByEmail() {
        final String email = "287y382h@gmail.com";
        Reader reader = createReader(email);

        readerRepository.save(reader);

        Optional<Reader> foundReader = readerRepository.findByEmail(email);
        assertThat(foundReader).isPresent()
                .hasValueSatisfying(r -> assertThat(r.getEmail()).isEqualTo(email));
    }

    @Test
    @DisplayName("Поиск несуществующего email")
    void testFindByEmailNotFound() {
        Optional<Reader> foundReader = readerRepository.findByEmail("nonexistentemail@gmail.com");

        assertThat(foundReader).isEmpty();
    }

    @Test
    @DisplayName("Сохранение неверного email")
    void testSaveWrongEmail() {
        final String email = "287y382hgmail.com";
        Reader reader = createReader(email);

        assertThatThrownBy(() -> readerRepository.save(reader)).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Сохранение пользователя")
    void testSaveReader() {
        final String email = "alex.smith@gmail.com";
        Reader reader = createReader(email);

        Reader savedReader = readerRepository.save(reader);

        assertThat(savedReader).isNotNull()
                .isEqualTo(reader);
    }

    private Reader createReader(String email) {
        Role role = Role.builder()
                .name("user")
                .build();
        role = roleRepository.save(role);
        return Reader.builder()
                .firstName("Maksim")
                .lastName("Ivanovich")
                .email(email)
                .familyName("Ivanovich")
                .username("booklover")
                .roles(new HashSet<>(Set.of(role)))
                .build();
    }
}
