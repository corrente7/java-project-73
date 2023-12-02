package hexlet.code.service;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    public Set<Label> addLabels(TaskDto taskDto) {

        Set<Label> labels = labelRepository.findByIdIn(taskDto.getLabelIds());

//        if (taskDto.getLabelsIds() != null) {
//            for (Long labelId : taskDto.getLabelsIds()) {
//                labels.add(labelRepository.findById(labelId).orElseThrow());
//            }
//        }
        return labels;

    }


}
