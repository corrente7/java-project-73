package hexlet.code.controller;


import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

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
    public TaskStatus createTaskStatus(@Valid @RequestBody TaskStatusDto taskStatusDto) {

        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName(taskStatusDto.getName());
        return taskStatusRepository.save(taskStatus);
    }

    @PutMapping(path = "/{id}")
    public TaskStatus updateTaskStatus(@RequestBody TaskStatusDto taskStatusDto, @PathVariable long id) {
        if (!taskStatusRepository.existsById(id)) {
            throw new NoSuchElementException("TaskStatus not found");
        }
        TaskStatus taskStatus = taskStatusRepository.findById(id).get();

        taskStatus.setName(taskStatusDto.getName());
        return taskStatusRepository.save(taskStatus);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteTaskStatus(@PathVariable long id) {
        if (!taskStatusRepository.existsById(id)) {
            throw new NoSuchElementException("TaskStatus not found");
        }
        taskStatusRepository.deleteById(id);
    }



}
