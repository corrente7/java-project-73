package hexlet.code.controller;


import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskServiceImpl;
import hexlet.code.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import com.querydsl.core.types.Predicate;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private TaskServiceImpl taskServiceImpl;

    @GetMapping(path = "")
    public List<Task> getTasks(@QuerydslPredicate (root = Task.class) Predicate predicate) {
        return predicate == null ? taskRepository.findAll() : taskRepository.findAll(predicate);
    }

    @GetMapping(path = "/{id}")
    public Task getTask(@PathVariable long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public Task createTask(@Valid @RequestBody TaskDto taskDto) {

        List <Label> labels = taskServiceImpl.addLabels(taskDto);

        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setAuthor(userDetailsServiceImpl.getCurrentUserName());
        task.setTaskStatus(taskStatusRepository.findById(taskDto.getTaskStatusId()).orElseThrow());
        task.setExecutor(taskDto.getExecutor());
        task.setLabels(labels);
        return taskRepository.save(task);
    }

    @PutMapping(path = "/{id}")
    public Task updateTask(@RequestBody TaskDto taskDto, @PathVariable long id) {
        if (!taskRepository.existsById(id)) {
            // Если не существует, возвращаем код ответа 404
            throw new NoSuchElementException("Task not found");
        }
        Task task = taskRepository.findById(id).get();

        List <Label> labels = taskServiceImpl.addLabels(taskDto);

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setExecutor(taskDto.getExecutor());
        task.setLabels(labels);
        return taskRepository.save(task);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteTask(@PathVariable long id) {
        if (!taskRepository.existsById(id)) {
            throw new NoSuchElementException("Task not found");
        }

        Task task = taskRepository.findById(id).orElseThrow();

        User currentUser = userDetailsServiceImpl.getCurrentUserName();
        User AuthorUser = task.getAuthor();

        if (!Objects.equals(currentUser, AuthorUser))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        taskRepository.deleteById(id);
    }
}
