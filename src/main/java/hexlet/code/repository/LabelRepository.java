package hexlet.code.repository;

import hexlet.code.model.Label;
import hexlet.code.model.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LabelRepository extends CrudRepository<Label, Long> {

    Optional<Label> findById(long id);
    List<Label> findAll();
}
