package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.JwtTokenUtil;
import hexlet.code.util.TestUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@SpringBootTest
@AutoConfigureMockMvc
public class UsersTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private ObjectMapper om;

    private User testUser;

    private String token;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final String baseUrl = "http://localhost:8080";


    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(testUtils.getUserModel())
                .create();
        userRepository.save(testUser);
        token = jwtTokenUtil.generateToken(testUser);
    }

    @AfterEach
    void clean() {
        testUtils.clean();
    }

    @Test
    public void getUsersTest() throws Exception {
        User testUser1 = Instancio.of(testUtils.getUserModel())
                .create();
        userRepository.save(testUser1);
        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/api/users"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(testUser.getFirstName());
        assertThat(response.getContentAsString()).contains(testUser.getLastName());
        assertThat(response.getContentAsString()).contains(testUser1.getFirstName());
        assertThat(response.getContentAsString()).contains(testUser1.getLastName());
        System.out.println(userRepository.findAll().size());
    }

    @Test
    public void getUserTest() throws Exception {

        Long id = testUser.getId();

        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/api/users/" + id)
                        .header("Authorization", "Bearer " + token))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(testUser.getFirstName());
        assertThat(response.getContentAsString()).contains(testUser.getLastName());
        assertThat(response.getContentAsString()).contains(testUser.getEmail());
    }

    @Test
    public void createUserTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Mark");
        userDto.setLastName("Walker");
        userDto.setEmail("mark@google.com");
        userDto.setPassword("12345");

        MockHttpServletResponse response = mockMvc
                .perform(post(baseUrl + "/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(userDto)))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(201);

        assertThat(userRepository.findUserByEmailIgnoreCase("mark@google.com").isPresent());
        assertThat(userRepository.findUserByEmailIgnoreCase("mark@google.com")
                .get().toString()).contains("Mark", "Walker");
    }

    @Test
    public void updateUserTest() throws Exception {

        Long id = testUser.getId();
        UserDto userDto = new UserDto("user@gmail.com", "Mike", testUser.getLastName(), "666777");

        MockHttpServletResponse response = mockMvc
                .perform(put(baseUrl + "/api/users/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(userDto)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(userRepository.findUserByEmailIgnoreCase("user@gmail.com").isPresent());
        assertThat(userRepository.findUserByEmailIgnoreCase("user@gmail.com").get().toString()).contains("Mike");
    }

    @Test
    public void deleteUserTest() throws Exception {

        MockHttpServletResponse response = mockMvc
                .perform(delete(baseUrl + "/api/users/" + testUser.getId())
                        .header("Authorization", "Bearer " + token)
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(userRepository.findById(testUser.getId())).isEmpty();
    }
}
