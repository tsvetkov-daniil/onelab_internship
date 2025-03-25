package tsvetkov.daniil.auth.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import tsvetkov.daniil.auth.entity.Reader;
import tsvetkov.daniil.auth.entity.Role;
import tsvetkov.daniil.auth.repository.ReaderRepository;
import tsvetkov.daniil.auth.exception.ReaderNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@RequiredArgsConstructor
@Service
@Validated
public class ReaderService {

    private final ReaderRepository readerRepository;
    private final RoleService roleService;
    private final UploadService uploadService;
    private ReaderService self;

    @Autowired
    @Lazy
    public void setSelf(ReaderService self) {
        this.self = self;
    }


    @Transactional
    public Reader save(@Valid Reader reader) {
        if (reader.getId() == null) {
            Role defaultRole = roleService.getDefaultRole();
            reader.setRoles(Set.of(defaultRole));
        } else if (!readerRepository.existsById(reader.getId())) {
            throw new ReaderNotFoundException();
        }
        return readerRepository.save(reader);
    }

    @Transactional(readOnly = true)
    public Optional<Reader> findByUsername(String username) {
        return readerRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<Reader> findById(Long id) {
        return readerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Reader> findByEmail(String email) {
        return readerRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Set<Reader> findAll(int pageNumber, int pageSize) {
        return new HashSet<>(readerRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent());
    }

    @Transactional
    public void deleteById(Long id) {
        if (!readerRepository.existsById(id)) {
            throw new ReaderNotFoundException();
        }
        readerRepository.deleteById(id);
    }

    @Transactional
    public Reader setReaderPhoto(Long userId, MultipartFile file) {

        String path = uploadService.uploadUserPhoto(userId, file);

        Reader reader = self.findById(userId).orElseThrow(ReaderNotFoundException::new);
        reader.setPhotoUrl(path);
        return readerRepository.save(reader);
    }
}
