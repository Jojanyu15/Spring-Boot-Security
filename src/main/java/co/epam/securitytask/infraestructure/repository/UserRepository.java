package co.epam.securitytask.infraestructure.repository;

import co.epam.securitytask.core.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    @Query(value = "SELECT u FROM User u where u.email = :email")
    Optional<User> findByEmail(String email);
}
