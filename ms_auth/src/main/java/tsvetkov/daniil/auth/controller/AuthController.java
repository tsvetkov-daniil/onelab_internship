package tsvetkov.daniil.auth.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsvetkov.daniil.auth.dto.Reader;
import tsvetkov.daniil.auth.dto.Role;
import tsvetkov.daniil.auth.service.ReaderService;
import tsvetkov.daniil.auth.service.RoleService;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final ReaderService readerService;
    private final RoleService roleService;

    @Autowired
    public AuthController(ReaderService readerService, RoleService roleService) {
        this.readerService = readerService;
        this.roleService = roleService;
    }

    // Эндпоинты для Reader
    @PostMapping("/readers")
    public ResponseEntity<Reader> createReader(@Valid @RequestBody Reader reader) throws Exception {
        Reader savedReader = readerService.save(reader);
        return new ResponseEntity<>(savedReader, HttpStatus.CREATED);
    }

    @GetMapping("/readers/{id}")
    public ResponseEntity<Reader> getReaderById(@PathVariable Long id) {
        Optional<Reader> reader = readerService.findById(id);
        return reader.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/readers/email/{email}")
    public ResponseEntity<Reader> getReaderByEmail(@PathVariable String email) {
        Optional<Reader> reader = readerService.findByEmail(email);
        return reader.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/readers")
    public ResponseEntity<Set<Reader>> getAllReaders(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        Set<Reader> readers = readerService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok(readers);
    }

    @PutMapping("/readers/{id}")
    public ResponseEntity<Reader> updateReader(@PathVariable Long id, @Valid @RequestBody Reader reader) throws Exception {
        reader.setId(id);
        Reader updatedReader = readerService.save(reader);
        return ResponseEntity.ok(updatedReader);
    }

    @DeleteMapping("/readers/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable Long id) throws Exception {
        readerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Эндпоинты для Role
    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        Role savedRole = roleService.save(role);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Optional<Role> role = roleService.findById(id);
        return role.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/roles/name/{name}")
    public ResponseEntity<Role> getRoleByName(@PathVariable String name) {
        Optional<Role> role = roleService.findByName(name);
        return role.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<Role>> getAllRoles(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        Set<Role> roles = roleService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok(roles);
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/roles")
    public ResponseEntity<Void> deleteAllRoles() {
        roleService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles/default")
    public ResponseEntity<Role> getDefaultRole() {
        Optional<Role> role = roleService.getDefaultRole();
        return role.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}