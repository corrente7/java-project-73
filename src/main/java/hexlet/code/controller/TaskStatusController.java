package hexlet.code.controller;


import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.service.TaskStatusService;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/statuses")
public class TaskStatusController {

    @Autowired
    private TaskStatusService taskStatusService;

    @Operation(summary = "Get all task statuses")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get list of task statuses")
    })
    @GetMapping(path = "")
    public List<TaskStatus> getTaskStatuses() {
        return taskStatusService.getTaskStatuses();
    }

    @Operation(summary = "Get task status by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get a specific task status by id")
    })
    @GetMapping(path = "/{id}")
    public TaskStatus getTaskStatus(@PathVariable long id) {
        return taskStatusService.getTaskStatus(id);
    }

    @Operation(summary = "Create new task status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task status successfully created"),
        @ApiResponse(responseCode = "422", description = "Data not valid")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public TaskStatus createTaskStatus(@Valid @RequestBody TaskStatusDto taskStatusDto) {
        return taskStatusService.createTaskStatus(taskStatusDto);
    }

    @Operation(summary = "Update task status by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task status successfully updated"),
        @ApiResponse(responseCode = "422", description = "Data not valid")
    })
    @PutMapping(path = "/{id}")
    public TaskStatus updateTaskStatus(@RequestBody TaskStatusDto taskStatusDto, @PathVariable long id) {
        return taskStatusService.updateTaskStatus(taskStatusDto, id);
    }

    @Operation(summary = "Delete task status by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Delete task status by id")
    })
    @DeleteMapping(path = "/{id}")
    public void deleteTaskStatus(@PathVariable long id) {
        taskStatusService.deleteTaskStatus(id);
    }

}
