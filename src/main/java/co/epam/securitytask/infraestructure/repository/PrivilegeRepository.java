package co.epam.securitytask.infraestructure.repository;

import co.epam.securitytask.core.model.Privilege;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends CrudRepository<Privilege,Long> {

    @Query(value = "SELECT p FROM Privilege p where p.name = :name")
    Privilege findByName(String name);
}
