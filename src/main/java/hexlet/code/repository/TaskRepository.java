package hexlet.code.repository;

import hexlet.code.model.Task;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import com.querydsl.core.types.Predicate;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long>,
        QuerydslPredicateExecutor<Task> {

    Optional<Task> findById(long id);

    List<Task> findAll(Predicate predicate);
    List<Task> findAll();

}
