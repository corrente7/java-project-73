package hexlet.code.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;

    private String description;

    @ManyToOne
    //@JoinColumn(name = "task_status_id")
    private TaskStatus taskStatus;

    @ManyToOne
   // @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    //@JoinColumn(name = "executor_id")
    private User executor;

    @CreationTimestamp
    private Date createdAt;

    @ManyToMany (fetch = FetchType.EAGER)
    private List<Label> labels;


}
