package hexlet.code.utils;


import hexlet.code.model.User;
import hexlet.code.model.UserRole;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.stereotype.Component;



@Component

public class TestUtils {

    private Model<User> userModel;

    


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

    }

    public Model<User> getUserModel() {
        return userModel;
    }
}