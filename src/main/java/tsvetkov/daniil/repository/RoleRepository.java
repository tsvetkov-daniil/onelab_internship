package tsvetkov.daniil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.dto.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.id = 0")
    Optional<Role> findDefaultRole();
    Optional<Role> findByName(String name);
}
