package hexlet.code;


import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.dto.UserDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusesTests {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TestUtils testUtils;

    @Autowired
    private ObjectMapper om;


    private TaskStatus testTaskStatus;

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
        testTaskStatus = Instancio.of(testUtils.getTaskStatusModel())
                .create();
        taskStatusRepository.save(testTaskStatus);
//        testUser = Instancio.of(testUtils.getUserModel())
//                .create();
//        userRepository.save(testUser);
//        token = jwtTokenUtil.generateToken(testUser);
    }


    @Test
    public void getTaskStatusesTest() throws Exception {
        TaskStatus testTaskStatus1 = Instancio.of(testUtils.getTaskStatusModel())
                .create();
        taskStatusRepository.save(testTaskStatus1);
        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/api/statuses"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(testTaskStatus.getName());
        assertThat(response.getContentAsString()).contains(testTaskStatus1.getName());

    }

    @Test
    public void getTaskStatusTest() throws Exception {

        Long id = testTaskStatus.getId();

        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/api/statuses/" + id))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(testTaskStatus.getName());
    }

    @Test
    public void createTaskStatusTest() throws Exception {
        TaskStatusDto testTaskStatusDto = new TaskStatusDto("New");

// почему-то работает без авторизации, не пойму почему
        MockHttpServletResponse response = mockMvc
                .perform(post(baseUrl + "/api/statuses")
                    //    .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(testTaskStatusDto)))
                .andReturn()
                .getResponse();


        assertThat(response.getStatus()).isEqualTo(201);

        MockHttpServletResponse response1 = mockMvc
                .perform(get(baseUrl + "/api/statuses"))
                .andReturn()
                .getResponse();

        assertThat(response1.getStatus()).isEqualTo(200);
        assertThat(response1.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response1.getContentAsString()).contains("New");
    }

    @Test
    public void updateTaskStatus() throws Exception {

        Long id = testTaskStatus.getId();
        TaskStatusDto testTaskStatusDto = new TaskStatusDto("Updated");

        MockHttpServletResponse response = mockMvc
                .perform(put(baseUrl + "/api/statuses/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(testTaskStatusDto)))
                .andReturn().getResponse();


        assertThat(response.getStatus()).isEqualTo(200);

        MockHttpServletResponse response1 = mockMvc
                .perform(get(baseUrl + "/api/statuses/" + id))
                .andReturn().getResponse();

        assertThat(response1.getContentAsString()).contains("Updated");

    }

    @Test
    public void deleteTaskStatus() throws Exception {

        MockHttpServletResponse response = mockMvc
                .perform(delete(baseUrl + "/api/statuses/" + testTaskStatus.getId())
                        .header("Authorization", "Bearer " + token)
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(userRepository.findById(testTaskStatus.getId())).isEmpty();
    }

}
