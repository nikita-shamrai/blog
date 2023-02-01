package ua.shamray.myblogspringbootv1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.shamray.myblogspringbootv1.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

}
