package hexlet.code.utils;


import hexlet.code.model.User;
import hexlet.code.model.UserRole;
import jakarta.annotation.PostConstruct;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.stereotype.Component;


@Component

public class TestUtils {

    private Model<User> userModel;


    @PostConstruct
    private void init() {
        //final int numberOfObjects = 5;
        final UserRole role = UserRole.valueOf("USER");
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .set(Select.field(User::getRole), role)
                .toModel();

    }

    public Model<User> getUserModel() {
        return userModel;
    }
}