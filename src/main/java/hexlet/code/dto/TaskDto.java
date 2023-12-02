package hexlet.code.dto;

import hexlet.code.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Long taskStatusId;

    private User executor;

    private Set<Long> labelIds;

    public Set<Long> getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(Set<Long> labelIds) {
        this.labelIds = labelIds;
    }
}
