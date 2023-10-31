package hexlet.code.repository;

import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskStatusRepository extends CrudRepository<TaskStatus, Long> {

    public Optional<TaskStatus> findById(Long id);
    List<TaskStatus> findAll();
}
