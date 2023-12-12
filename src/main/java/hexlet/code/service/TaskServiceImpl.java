package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TaskServiceImpl {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public Task createTask(TaskDto taskDto) {
        Set<Label> labels = addLabels(taskDto);

        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setAuthor(userDetailsServiceImpl.getCurrentUserName());
        task.setTaskStatus(taskStatusRepository.findById(taskDto.getTaskStatusId()).orElseThrow());
        task.setExecutor(taskDto.getExecutor());
        task.setLabels(labels);
        return taskRepository.save(task);
    }

    public Task updateTask(TaskDto taskDto, long id) {
        if (!taskRepository.existsById(id)) {
            throw new NoSuchElementException("Task not found");
        }
        Task task = taskRepository.findById(id).get();

        Set<Label> labels = addLabels(taskDto);

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setExecutor(taskDto.getExecutor());
        task.setLabels(labels);
        return taskRepository.save(task);
    }

    public void deleteTask(long id) {
        if (!taskRepository.existsById(id)) {
            throw new NoSuchElementException("Task not found");
        }

        Task task = taskRepository.findById(id).orElseThrow();

        User currentUser = userDetailsServiceImpl.getCurrentUserName();
        User authorUser = task.getAuthor();

        if (!Objects.equals(currentUser, authorUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        taskRepository.deleteById(id);
    }

    public Task getTask(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
    }

    public List<Task> getTasks(Predicate predicate) {
        return predicate == null ? taskRepository.findAll() : taskRepository.findAll(predicate);
    }



    public Set<Label> addLabels(TaskDto taskDto) {

        Set<Label> labels = labelRepository.findByIdIn(taskDto.getLabelIds());
        return labels;

    }


}
