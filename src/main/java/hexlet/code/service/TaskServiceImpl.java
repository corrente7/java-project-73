package hexlet.code.service;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    public List<Label> addLabels(TaskDto taskDto) {

        List<Label> labels = new ArrayList<>();

        if (taskDto.getLabelsIds() != null) {
            for (Long labelId : taskDto.getLabelsIds()) {
                labels.add(labelRepository.findById(labelId).orElseThrow());
            }
        }
        return labels;

    }


}
