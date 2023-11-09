package hexlet.code.repository;


import hexlet.code.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByEmailIgnoreCase(String email);

    Optional<User> findById(long id);
    List<User> findAll();
}
