package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import hexlet.code.dto.LabelDto;
import hexlet.code.dto.LoginDto;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.UserDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.JwtTokenUtil;
import hexlet.code.util.TestUtils;
import org.instancio.Instancio;
import org.instancio.Select;
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
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@SpringBootTest
@AutoConfigureMockMvc
public class LabelsTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private ObjectMapper om;


    private User testUser;

    private TaskStatus testTaskStatus;

    private Task testTask;

    private Label testLabel;

    private String token;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final String baseUrl = "http://localhost:8080";

    @Autowired
    private WebApplicationContext webApplicationContext;


    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(testUtils.getUserModel())
                .create();
        userRepository.save(testUser);
        token = jwtTokenUtil.generateToken(testUser);

        testLabel = Instancio.of(testUtils.getLabelModel())
                .create();
        labelRepository.save(testLabel);

    }

    @AfterEach
    void clean() {
        testUtils.clean();
    }

    @Test
    public void getLabelsTest() throws Exception {

        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/api/labels")
                .header("Authorization", "Bearer " + token))
                .andReturn()
                .getResponse();

               assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(testLabel.getName());

    }

    @Test
    public void getLabelTest() throws Exception {

        Long id = testLabel.getId();

        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/api/labels/" + id)
                        .header("Authorization", "Bearer " + token))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(testLabel.getName());
    }

    @Test
    public void createLabelTest() throws Exception {
        LabelDto labelDto = new LabelDto();
        labelDto.setName("NewLabel");

        MockHttpServletResponse response = mockMvc
                .perform(post(baseUrl + "/api/labels")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(labelDto)))
                .andReturn()
                .getResponse();


        assertThat(response.getStatus()).isEqualTo(201);

        MockHttpServletResponse response1 = mockMvc
                .perform(get(baseUrl + "/api/labels")
                        .header("Authorization", "Bearer " + token))
                .andReturn()
                .getResponse();

        assertThat(response1.getStatus()).isEqualTo(200);
        assertThat(response1.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response1.getContentAsString()).contains("NewLabel");
    }

    @Test
    public void updateLabelTest() throws Exception {

        Long id = testLabel.getId();
        LabelDto labelDto = new LabelDto();
        labelDto.setName("NewLabel");

        MockHttpServletResponse response = mockMvc
                .perform(put(baseUrl + "/api/labels/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(labelDto)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        MockHttpServletResponse response1 = mockMvc
                .perform(get(baseUrl + "/api/labels/" + id)
                        .header("Authorization", "Bearer " + token))
                .andReturn().getResponse();

        assertThat(response1.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response1.getContentAsString()).contains("NewLabel");

    }

    @Test
    public void deleteLabelTest() throws Exception {

        MockHttpServletResponse response = mockMvc
                .perform(delete(baseUrl + "/api/labels/" + testLabel.getId())
                        .header("Authorization", "Bearer " + token)
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(labelRepository.findById(testLabel.getId())).isEmpty();
    }


}