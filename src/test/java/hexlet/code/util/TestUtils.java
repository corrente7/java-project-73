package hexlet.code.util;


import hexlet.code.model.*;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class TestUtils {

    private Model<User> userModel;

    private Model<TaskStatus> taskStatusModel;

    private Model<Task> taskModel;

    private Model<Label> labelModel;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;


    @PostConstruct
    private void init() {
        Faker faker = new Faker();
        final UserRole role = UserRole.valueOf("USER");
        String email = faker.internet().emailAddress();

        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .set(Select.field(User::getEmail), email)
                .set(Select.field(User::getRole), role)
                .toModel();

        taskStatusModel = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .toModel();

        taskModel = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .toModel();

        labelModel = Instancio.of(Label.class)
                .ignore(Select.field(Label::getId))
                .toModel();

    }

    public void clean() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();
        userRepository.deleteAll();
    }

    public Model<User> getUserModel() {
        return userModel;
    }

    public Model<TaskStatus> getTaskStatusModel() {
        return taskStatusModel;
    }

    public Model<Task> getTaskModel() {
        return taskModel;
    }

    public Model<Label> getLabelModel() {
        return labelModel;
    }
}