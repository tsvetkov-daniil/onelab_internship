package tsvetkov.daniil.auth.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.auth.dto.Role;
import tsvetkov.daniil.auth.repository.RoleRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Role save(@Valid Role role) {
        return roleRepository.save(role);
    }

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Set<Role> findAll(int pageNumber, int pageSize) {
        return new HashSet<>(roleRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent());
    }


    @Transactional
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        roleRepository.deleteAll();
    }

    @Transactional
    public Optional<Role> getDefaultRole() {
        final Optional<Role> role = this.findById(0L);
        if (role.isPresent()) {
            return role;
        } else {
            return Optional.of(roleRepository.save(Role.builder()
                    .name("user")
                    .build()));
        }
    }
}
