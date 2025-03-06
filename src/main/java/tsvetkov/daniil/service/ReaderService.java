package tsvetkov.daniil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.dto.Reader;
import tsvetkov.daniil.dto.Role;
import tsvetkov.daniil.repository.ReaderRepository;
import tsvetkov.daniil.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReaderService {
    private final ReaderRepository readerRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public ReaderService(ReaderRepository readerRepository, RoleRepository roleRepository) {
        this.readerRepository = readerRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Reader create(Reader reader) {
        Role defaultRole = roleRepository.findDefaultRole()
                .orElseThrow(() -> new RuntimeException("Дефолтная роль не найдена"));

        reader.setRole(defaultRole);

        return readerRepository.save(reader);
    }

    public Optional<Reader> findById(Long id) {
        return readerRepository.findById(id);
    }

    public Optional<Reader> findByEmail(String email) {
        return readerRepository.findByEmail(email);
    }

    public List<Reader> findAll() {
        return readerRepository.findAll();
    }

    @Transactional
    public Reader update(Long id, Reader updatedReader) {
        Reader existingReader = readerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        existingReader.setFirstName(updatedReader.getFirstName());
        existingReader.setLastName(updatedReader.getLastName());
        existingReader.setFamilyName(updatedReader.getFamilyName());
        existingReader.setNickname(updatedReader.getNickname());
        existingReader.setEmail(updatedReader.getEmail());
        existingReader.setAboutMe(updatedReader.getAboutMe());
        existingReader.setPhotoUrl(updatedReader.getPhotoUrl());
        existingReader.setRole(updatedReader.getRole());
        existingReader.setBooks(updatedReader.getBooks());
        existingReader.setFavorite(updatedReader.getFavorite());

        return readerRepository.save(existingReader);
    }

    @Transactional
    public void delete(Long id) {
        readerRepository.deleteById(id);
    }
}
