package tsvetkov.daniil.auth.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tsvetkov.daniil.auth.entity.Role;
import tsvetkov.daniil.auth.repository.RoleRepository;
import tsvetkov.daniil.auth.exception.RoleNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
@Validated
public class RoleService {

    private static final String DEFAULT_ROLE_NAME = "USER";

    private final RoleRepository roleRepository;

    @Transactional
    public Role save(@Valid Role role) {
        return roleRepository.save(role);
    }

    @Transactional(readOnly = true)
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
    @Transactional(readOnly = true)
    public Set<Role> findAll(int pageNumber, int pageSize) {
        return new HashSet<>(roleRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent());
    }

    @Transactional
    public void deleteById(Long id)  {
        if (!roleRepository.existsById(id)) {
            throw new RoleNotFoundException();
        }
        roleRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        roleRepository.deleteAll();
    }

    @Transactional
    public Role getDefaultRole() {
        return findByName(DEFAULT_ROLE_NAME)
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name(DEFAULT_ROLE_NAME)
                        .build()));
    }
}
