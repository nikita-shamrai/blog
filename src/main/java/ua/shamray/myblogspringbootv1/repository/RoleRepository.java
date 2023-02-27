package ua.shamray.myblogspringbootv1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shamray.myblogspringbootv1.entity.Role;
import ua.shamray.myblogspringbootv1.entity.RoleType;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByRoleType(RoleType roleType);

}
