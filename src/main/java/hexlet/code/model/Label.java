package hexlet.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Table(name = "labels")
@NoArgsConstructor
@AllArgsConstructor
public class Label {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @CreationTimestamp
    private Date createdAt;

    @ManyToMany
    @JoinTable(name="label_task",
            joinColumns = @JoinColumn(name="label_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="task_id", referencedColumnName="id")
    )
    private List<Task> tasks;
}
