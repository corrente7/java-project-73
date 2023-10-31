package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import hexlet.code.model.User;
import hexlet.code.model.UserRole;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.JwtTokenUtil;
import hexlet.code.utils.TestUtils;
import io.jsonwebtoken.Jwts;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;


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
        System.out.println(testUser);
        userRepository.save(testUser);
        token = jwtTokenUtil.generateToken(testUser);
    }


    @Test
    public void getUsersTest() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/api/users"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        //assertThatJson(response.getContentAsString()).isArray();
        assertThat(response.getContentAsString()).contains(testUser.getFirstName());
        assertThat(response.getContentAsString()).contains(testUser.getLastName());

//        final List<User> userList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() { });
//        final List<User> expected = userRepository.findAll();
//        assertThat(expected.size()).isEqualTo(userList.size());
//        assertThat(userList).containsAll(expected);
    }

    @Test
    public void testUpdate() throws Exception {

        var data = new HashMap<>();
        data.put("firstName", "Mike");

        var user = userRepository.findById(testUser.getId()).get();

        MockHttpServletResponse response = mockMvc
                .perform(put(baseUrl + "/api/users/" + user.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data)))
                .andReturn().getResponse();

        assertNotNull(token);

        assertThat(response.getStatus()).isEqualTo(200);

        assertThat(user.getFirstName()).isEqualTo(("Mike"));
    }
}