package hexlet.code.controller;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.service.LabelService;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @Operation(summary = "Get all labels")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get list of labels")
    })
    @GetMapping(path = "")
    public List<Label> getLabels() {

        return labelService.getLabels();
    }


    @Operation(summary = "Get label by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get a specific label by id")
    })
    @GetMapping(path = "/{id}")
    public Label getLabel(@PathVariable long id) {
        return labelService.getLabel(id);
    }


    @Operation(summary = "Create new label")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Label successfully created"),
        @ApiResponse(responseCode = "422", description = "Data not valid")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public Label createLabel(@Valid @RequestBody LabelDto labelDto) {
        return labelService.createLabel(labelDto);
    }

    @Operation(summary = "Update label by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Label successfully updated"),
        @ApiResponse(responseCode = "422", description = "Data not valid")
    })
    @PutMapping(path = "/{id}")
    public Label updateLabel(@RequestBody LabelDto labelDto, @PathVariable long id) {
        return labelService.updateLabel(labelDto, id);
    }

    @Operation(summary = "Delete label by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Delete label by id")
    })
    @DeleteMapping(path = "/{id}")
    public void deleteLabel(@PathVariable long id) {
        labelService.deleteLabel(id);
    }
}
