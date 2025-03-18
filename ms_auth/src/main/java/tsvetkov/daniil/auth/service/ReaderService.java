package tsvetkov.daniil.auth.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.auth.dto.Reader;
import tsvetkov.daniil.auth.dto.Role;
import tsvetkov.daniil.auth.repository.ReaderRepository;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class ReaderService {
    private final ReaderRepository readerRepository;
    private final RoleService roleService;

    @Autowired
    public ReaderService(ReaderRepository readerRepository, RoleService roleService) {
        this.readerRepository = readerRepository;
        this.roleService = roleService;
    }

    @Transactional
    public Reader save(@Valid Reader reader) throws Exception {
        if (Objects.isNull(reader.getId())) {
            Role defaultRole = roleService.getDefaultRole()
                    .orElseThrow(() -> new RuntimeException("Дефолтная роль не найдена"));
            reader.setRole(defaultRole);
        } else if (!readerRepository.existsById(reader.getId())) {
            reader.setId(null);
        }
        return readerRepository.save(reader);
    }

    public Optional<Reader> findById(Long id) {
        return readerRepository.findById(id);
    }

    public Optional<Reader> findByEmail(String email) {
        return readerRepository.findByEmail(email);
    }

    public Set<Reader> findAll(int pageNumber, int pageSize) {
        return new HashSet<>(readerRepository.findAll(PageRequest.of(pageNumber,pageSize)).getContent());
    }


    @Transactional
    public void deleteById(Long id) throws Exception {
        if (readerRepository.existsById(id))
            readerRepository.deleteById(id);
        else throw new Exception("Пользователь не найден");
    }
}
