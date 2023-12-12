package hexlet.code.repository;

import hexlet.code.model.Label;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LabelRepository extends CrudRepository<Label, Long> {

    Optional<Label> findById(long id);
    Optional<Label> findByName(String name);
    Set<Label> findByIdIn(Set<Long> id);
    List<Label> findAll();
}
