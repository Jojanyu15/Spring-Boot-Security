package co.epam.securitytask.infraestructure.repository;

import co.epam.securitytask.core.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    @Query(value = "SELECT r FROM Role r where r.name = :name")
    Role findByName(String name);
}
