package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import hexlet.code.dto.LoginDto;
import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.JwtTokenUtil;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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

    @Autowired
    private WebApplicationContext webApplicationContext;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        testUser = Instancio.of(testUtils.getUserModel())
                .create();
        userRepository.save(testUser);
        token = jwtTokenUtil.generateToken(testUser);
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

        MockHttpServletResponse response1 = mockMvc
                .perform(get(baseUrl + "/api/users"))
                .andReturn()
                .getResponse();

        assertThat(response1.getStatus()).isEqualTo(200);
        assertThat(response1.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response1.getContentAsString()).contains("Mark", "Walker", "mark@google.com");
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

        assertNotNull(token);

        assertThat(response.getStatus()).isEqualTo(200);

        MockHttpServletResponse response1 = mockMvc
                .perform(get(baseUrl + "/api/users/" + id)
                        .header("Authorization", "Bearer " + token))
                .andReturn().getResponse();

        assertThat(response1.getContentAsString()).contains("Mike");
        assertThat(response.getContentAsString()).contains("user@gmail.com");

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