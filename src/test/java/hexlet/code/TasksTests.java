package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.dto.TaskDto;

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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@SpringBootTest
@AutoConfigureMockMvc
public class TasksTests {

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

    private Label testLabel1;

    private Label testLabel2;

    private Task testTask;

    private String token;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final String baseUrl = "http://localhost:8080";

    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(testUtils.getUserModel())
                .create();
        userRepository.save(testUser);
        testTaskStatus = Instancio.of(testUtils.getTaskStatusModel())
                .create();
        taskStatusRepository.save(testTaskStatus);
        testLabel1 = Instancio.of(testUtils.getLabelModel())
                .create();
        labelRepository.save(testLabel1);

        testLabel2 = Instancio.of(testUtils.getLabelModel())
                .create();
        labelRepository.save(testLabel2);

        testTask = Instancio.of(testUtils.getTaskModel())
                .set(Select.field(Task::getAuthor), testUser)
                .set(Select.field(Task::getExecutor), testUser)
                .set(Select.field(Task::getTaskStatus),  testTaskStatus)
                .set(Select.field(Task::getLabels),  Set.of(testLabel1, testLabel2))
                .create();
        taskRepository.save(testTask);

        token = jwtTokenUtil.generateToken(testUser);
    }

    @AfterEach
    void clean() {
        testUtils.clean();
    }
    @Test
    public void getTasksTest() throws Exception {
        Task testTask1 = Instancio.of(testUtils.getTaskModel())
                .set(Select.field(Task::getAuthor), testUser)
                .set(Select.field(Task::getExecutor), testUser)
                .set(Select.field(Task::getTaskStatus),  testTaskStatus)
                .set(Select.field(Task::getLabels),  Set.of(testLabel1, testLabel2))
                .create();
        taskRepository.save(testTask1);
        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/api/tasks")
                .header("Authorization", "Bearer " + token))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(taskRepository.findAll().size()).isEqualTo(2);
        assertThat(response.getContentAsString()).contains(testTask.getName());
        assertThat(response.getContentAsString()).contains(testTask.getTaskStatus().getName());
        assertThat(response.getContentAsString()).contains(testTask.getAuthor().getEmail());
    }

    @Test
    public void getTaskTest() throws Exception {

        Long id = testTask.getId();

        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/api/tasks/" + id)
                .header("Authorization", "Bearer " + token))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(testTask.getDescription());
        assertThat(response.getContentAsString()).contains(testTask.getAuthor().getFirstName());
        assertThat(response.getContentAsString()).contains(testTask.getAuthor().getLastName());
        assertThat(response.getContentAsString()).contains(testTask.getTaskStatus().getName());
    }

    @Test
    public void createTaskTest() throws Exception {

        TaskDto taskDto = new TaskDto();
        taskDto.setName("Edit");
        taskDto.setDescription("Edit text with comments");
        taskDto.setExecutor(testUser);
        taskDto.setTaskStatusId(testTaskStatus.getId());
        taskDto.setLabelIds(new HashSet<>());

        MockHttpServletResponse response = mockMvc
                .perform(post(baseUrl + "/api/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(taskDto)))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(201);

        assertThat(taskRepository.findByName("Edit").isPresent());
        assertThat(taskRepository.findByName("Edit").get().toString()).contains("Edit text with comments");
    }

    @Test
    public void updateTaskTest() throws Exception {

        Long id = testTask.getId();
        TaskDto taskDto = new TaskDto();
        taskDto.setName("Edit");
        taskDto.setDescription("Edit text with comments");
        taskDto.setExecutor(testUser);
        taskDto.setTaskStatusId(testTaskStatus.getId());
        taskDto.setLabelIds(new HashSet<>());

        MockHttpServletResponse response = mockMvc
                .perform(put(baseUrl + "/api/tasks/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(taskDto)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        assertThat(taskRepository.findById(id).get().toString()).contains("Edit");
        assertThat(taskRepository.findById(id).get().toString()).contains("Edit text with comments");
    }

    @Test
    public void deleteTaskTest() throws Exception {

        MockHttpServletResponse response = mockMvc
                .perform(delete(baseUrl + "/api/tasks/" + testTask.getId())
                        .header("Authorization", "Bearer " + token)
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(taskRepository.findById(testTask.getId())).isEmpty();
    }

    @Test
    public void getTasksWithParamsTest() throws Exception {

        Task testTask1 = Instancio.of(testUtils.getTaskModel())
                .set(Select.field(Task::getAuthor), testUser)
                .set(Select.field(Task::getExecutor), testUser)
                .set(Select.field(Task::getTaskStatus),  testTaskStatus)
                .set(Select.field(Task::getLabels),  Set.of(testLabel1, testLabel2))
                .create();

        taskRepository.save(testTask1);

        MockHttpServletResponse response = mockMvc
                .perform(
                        get(baseUrl + "/tasks" + "?authorId=" + testUser.getId())
                                .header("Authorization", "Bearer " + token)
                )
                .andReturn().getResponse();

        final List<Task> expected1 = taskRepository.findAll().stream()
                .filter(task -> Objects.equals(
                        task.getLabels().stream().map(Label::getId), testLabel1.getId()))
                .collect(Collectors.toList());

        final List<Task> expected = taskRepository.findAll().stream()
                .filter(task -> Objects.equals(task.getAuthor().getId(), testUser.getId()))
                .collect(Collectors.toList());

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(expected.size()).isEqualTo(2);
        assertThat(expected.size()).isEqualTo(2);
    }
}
