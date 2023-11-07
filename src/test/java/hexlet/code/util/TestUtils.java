package hexlet.code.util;


import hexlet.code.model.*;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.stereotype.Component;



@Component
public class TestUtils {

    private Model<User> userModel;

    private Model<TaskStatus> taskStatusModel;

    private Model<Task> taskModel;

    private Model<Label> labelModel;


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