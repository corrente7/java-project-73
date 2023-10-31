package hexlet.code.controller;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    @Autowired
    private LabelRepository labelRepository;

    @GetMapping(path = "/{id}")
    public Label getLabel(@PathVariable long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Label not found"));
    }

    @PostMapping(path = "")
    public Label createLabel(@Valid @RequestBody LabelDto labelDto) {

        Label label = new Label();
        label.setName(labelDto.getName());
        return labelRepository.save(label);
    }

    @PatchMapping(path = "/{id}")
    public Label updateLabel(@RequestBody LabelDto labelDto, @PathVariable long id) {
        if (!labelRepository.existsById(id)) {
            // Если не существует, возвращаем код ответа 404
            throw new NoSuchElementException("Label not found");
        }
        Label label = labelRepository.findById(id).get();

        label.setName(labelDto.getName());
        return labelRepository.save(label);
    }

    public void deleteLabel(@PathVariable long id) {
        if (!labelRepository.existsById(id)) {
            // Если не существует, возвращаем код ответа 404
            throw new NoSuchElementException("Label not found");
        }
        labelRepository.deleteById(id);
    }
}
