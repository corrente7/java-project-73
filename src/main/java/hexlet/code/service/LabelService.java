package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LabelService {

    @Autowired
    private LabelRepository labelRepository;

    public List<Label> getLabels() {

        return labelRepository.findAll();
    }

    public Label getLabel(long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Label not found"));
    }

    public Label createLabel(LabelDto labelDto) {

        Label label = new Label();
        label.setName(labelDto.getName());
        return labelRepository.save(label);
    }

    public Label updateLabel(LabelDto labelDto, long id) {
        Label label = labelRepository.findById(id).orElseThrow();

        label.setName(labelDto.getName());
        return labelRepository.save(label);
    }

    public void deleteLabel(long id) {
        labelRepository.deleteById(id);
    }

}
