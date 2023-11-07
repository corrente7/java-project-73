package hexlet.code.controller;


import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/statuses")
public class TaskStatusController {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @GetMapping(path = "")
    public List<TaskStatus> getTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public TaskStatus getTaskStatus(@PathVariable long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TaskStatus not found"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public TaskStatus createTaskStatus(@Valid @RequestBody TaskStatusDto taskStatusDto, @AuthenticationPrincipal UserDetails userDetails) {

        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName(taskStatusDto.getName());
        return taskStatusRepository.save(taskStatus);
    }

    @PutMapping(path = "/{id}")
    public TaskStatus updateTaskStatus(@RequestBody TaskStatusDto taskStatusDto, @PathVariable long id) {
        if (!taskStatusRepository.existsById(id)) {
            // Если не существует, возвращаем код ответа 404
            throw new NoSuchElementException("TaskStatus not found");
        }
        TaskStatus taskStatus = taskStatusRepository.findById(id).get();

        taskStatus.setName(taskStatusDto.getName());
        return taskStatusRepository.save(taskStatus);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteTaskStatus(@PathVariable long id) {
        if (!taskStatusRepository.existsById(id)) {
            // Если не существует, возвращаем код ответа 404
            throw new NoSuchElementException("TaskStatus not found");
        }
        taskStatusRepository.deleteById(id);
    }



}
