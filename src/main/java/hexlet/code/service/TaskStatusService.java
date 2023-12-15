package hexlet.code.service;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public List<TaskStatus> getTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    public TaskStatus getTaskStatus(long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TaskStatus not found"));
    }

    public TaskStatus createTaskStatus(TaskStatusDto taskStatusDto) {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName(taskStatusDto.getName());
        return taskStatusRepository.save(taskStatus);
    }

    public TaskStatus updateTaskStatus(TaskStatusDto taskStatusDto, long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id).orElseThrow();
        taskStatus.setName(taskStatusDto.getName());
        return taskStatusRepository.save(taskStatus);
    }

    public void deleteTaskStatus(long id) {
        taskStatusRepository.findById(id).orElseThrow();
        taskStatusRepository.deleteById(id);
    }
}
