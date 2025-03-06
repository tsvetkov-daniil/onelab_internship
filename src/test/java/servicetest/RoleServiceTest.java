package servicetest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tsvetkov.daniil.dto.Role;
import tsvetkov.daniil.repository.RoleRepository;
import tsvetkov.daniil.service.RoleService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void testCreateRole() {
        Role role = Role.builder()
                .name("Admin")
                .build();

        Mockito.when(roleRepository.save(role)).thenReturn(role);

        Role createdRole = roleService.create(role);

        assertNotNull(createdRole);
        assertEquals("Admin", createdRole.getName());
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Role role = Role.builder()
                .id(id)
                .name("Admin")
                .build();

        Mockito.when(roleRepository.findById(id)).thenReturn(Optional.of(role));

        Optional<Role> foundRole = roleService.findById(id);

        assertTrue(foundRole.isPresent());
        assertEquals(id, foundRole.get().getId());
    }

    @Test
    public void testFindByName() {
        String name = "Admin";
        Role role = Role.builder()
                .name(name)
                .build();

        Mockito.when(roleRepository.findByName(name)).thenReturn(Optional.of(role));

        Optional<Role> foundRole = roleService.findByName(name);

        assertTrue(foundRole.isPresent());
        assertEquals(name, foundRole.get().getName());
    }

    @Test
    public void testFindAll() {
        Role role1 = Role.builder().name("Admin").build();
        Role role2 = Role.builder().name("User").build();
        List<Role> roles = Arrays.asList(role1, role2);

        Mockito.when(roleRepository.findAll()).thenReturn(roles);

        List<Role> allRoles = roleService.findAll();

        assertNotNull(allRoles);
        assertEquals(2, allRoles.size());
    }

    @Test
    public void testUpdateRole() {
        Long id = 1L;
        Role existingRole = Role.builder()
                .id(id)
                .name("Admin")
                .build();

        Role updatedRole = Role.builder()
                .name("SuperAdmin")
                .build();

        Mockito.when(roleRepository.findById(id)).thenReturn(Optional.of(existingRole));
        Mockito.when(roleRepository.save(existingRole)).thenReturn(existingRole);

        Role updated = roleService.update(id, updatedRole);

        assertNotNull(updated);
        assertEquals("SuperAdmin", updated.getName());
    }

    @Test
    public void testDeleteRole() {
        Long id = 1L;

        Mockito.doNothing().when(roleRepository).deleteById(id);

        roleService.delete(id);

        Mockito.verify(roleRepository, Mockito.times(1)).deleteById(id);
    }
}

