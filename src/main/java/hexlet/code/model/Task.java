package hexlet.code.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Set;

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
    private TaskStatus taskStatus;

    @ManyToOne
    private User author;

    @ManyToOne
    private User executor;

    @CreationTimestamp
    private Date createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Label> labels;


}
