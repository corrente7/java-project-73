package hexlet.code.controller;


import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskServiceImpl;
import hexlet.code.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
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

import com.querydsl.core.types.Predicate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    @Operation(summary = "Get all tasks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get list of tasks")
    })
    @GetMapping(path = "")
    public List<Task> getTasks(@QuerydslPredicate (root = Task.class) Predicate predicate) {
        return taskServiceImpl.getTasks(predicate);
    }

    @Operation(summary = "Get task by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get a specific task by id")
    })
    @GetMapping(path = "/{id}")
    public Task getTask(@PathVariable long id) {
        return taskServiceImpl.getTask(id);
    }

    @Operation(summary = "Create new task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task successfully created"),
        @ApiResponse(responseCode = "422", description = "Data not valid")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public Task createTask(@Valid @RequestBody TaskDto taskDto) {
        return taskServiceImpl.createTask(taskDto);
    }

    @Operation(summary = "Update task by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task successfully updated"),
        @ApiResponse(responseCode = "422", description = "Data not valid")
    })
    @PutMapping(path = "/{id}")
    public Task updateTask(@RequestBody TaskDto taskDto, @PathVariable long id) {
        return taskServiceImpl.updateTask(taskDto, id);
    }

    @Operation(summary = "Delete task by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Delete task by id")
    })
    @DeleteMapping(path = "/{id}")
    public void deleteTask(@PathVariable long id) {
        taskServiceImpl.deleteTask(id);
    }
}
