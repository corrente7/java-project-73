package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.LoginDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.TestUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUtils testUtil;

    @Autowired
    private ObjectMapper om;

    private User testUser;

    private final String baseUrl = "http://localhost:8080";


    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(testUtil.getUserModel())
                .create();
        userRepository.save(testUser);
    }

    @Test
    public void loginUserTest() throws Exception {
        LoginDto credentials = new LoginDto(testUser.getEmail(), testUser.getPassword());

        MockHttpServletResponse login = mockMvc
                .perform(post(baseUrl + "/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(credentials)))
                .andReturn()
                .getResponse();

        assertThat(login.getStatus()).isEqualTo(200);
    }

    @Test
    public void loginNonValidTest() throws Exception {
        LoginDto credentials = new LoginDto("ppp@gmail.com", testUser.getPassword());

        MockHttpServletResponse login = mockMvc
                .perform(post(baseUrl + "/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(credentials)))
                .andReturn()
                .getResponse();

        assertThat(login.getStatus()).isEqualTo(403);
    }
}
