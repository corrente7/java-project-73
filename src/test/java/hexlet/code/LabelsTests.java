package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
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
public class LabelsTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private ObjectMapper om;


    private User testUser;

    private Task testTask;

    private Label testLabel;

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

        Label testLabel1 = Instancio.of(testUtils.getLabelModel())
                .create();
        labelRepository.save(testLabel1);
        Label testLabel2 = Instancio.of(testUtils.getLabelModel())
                .create();
        labelRepository.save(testLabel2);

        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/api/labels")
                .header("Authorization", "Bearer " + token))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(testLabel.getName());
        assertThat(labelRepository.findAll().size()).isEqualTo(3);
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
        assertThat(labelRepository.findByName("NewLabel").isPresent());
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
        assertThat(taskRepository.findByName("NewLabel").isPresent());
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
