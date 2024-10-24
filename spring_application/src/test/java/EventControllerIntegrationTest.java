import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.matthew.SpringApplication;
import ru.matthew.dao.model.Event;
import ru.matthew.dao.model.Location;
import ru.matthew.dao.repository.EventRepository;
import ru.matthew.dao.repository.LocationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(classes = SpringApplication.class)
@AutoConfigureMockMvc
public class EventControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EventRepository eventRepository;

    private static final String BASE_URL = "/api/events";

    private Long eventId;

    @BeforeEach
    void setUp() {
        // Arrange
        Location location = new Location();
        location.setSlug("msk");
        location.setName("Москва");
        locationRepository.save(location);

        Event event = new Event();
        event.setTitle("Ночь кино");
        event.setDate(LocalDate.of(2024, 10, 25));
        event.setDescription("Будет 5 фильмов");
        event.setPrice(BigDecimal.valueOf(250));
        event.setLocation(location);
        eventId = eventRepository.save(event).getId();
    }

    @AfterEach
    void tearDown() {
        locationRepository.deleteAll();
        eventRepository.deleteAll();
    }

    private String createEventJson(String title, String date, String description, BigDecimal price, String locationSlug) {
        return String.format("""
                {
                    "title": "%s",
                    "date": "%s",
                    "description": "%s",
                    "price": %s,
                    "location": "%s"
                }""", title, date, description, price, locationSlug);
    }

    @Test
    void searchEvents_WhenValidParams_ReturnsEvents() throws Exception {
        // Arrange & Act
        mockMvc.perform(get(BASE_URL)
                        .param("title", "Ночь кино")
                        .param("locationSlug", "msk")
                        .param("fromDate", "2024-10-24")
                        .param("toDate", "2024-10-26"))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Ночь кино"));
    }

    @ParameterizedTest
    @MethodSource("eventCreationData")
    void createEvent_WhenValidParams_ReturnsSuccess(String title, String date, String description, BigDecimal price, String locationSlug) throws Exception {
        // Arrange
        String eventJson = createEventJson(title, date, description, price, locationSlug);

        // Act & Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("invalidEventCreationData")
    void createEvent_WhenInvalidParams_ReturnsError(String title, String date, String description, BigDecimal price, String locationSlug, int expectedStatus) throws Exception {
        // Arrange
        String eventJson = createEventJson(title, date, description, price, locationSlug);

        // Act & Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().is(expectedStatus));
    }

    private static Object[][] eventCreationData() {
        return new Object[][] {
                {"Ночь кино 2", "2024-10-25", "Будет 5 фильмов", BigDecimal.valueOf(250), "msk"}
        };
    }

    private static Object[][] invalidEventCreationData() {
        return new Object[][] {
                {"", "2024-10-25", "Будет 5 фильмов", BigDecimal.valueOf(250), "msk", 400},
                {"Ночь кино", "invalid-date", "Будет 5 фильмов", BigDecimal.valueOf(250), "msk", 400},
                {"Ночь кино", "2024-10-25", "Будет 5 фильмов", BigDecimal.valueOf(250), "unknown-slug", 404},
                {"Ночь кино", "2024-10-25", "Будет 5 фильмов", BigDecimal.valueOf(-100), "msk", 400},
                {"Ночь кино", "2024-10-25", "Будет 5 фильмов", BigDecimal.valueOf(250), "", 404}
        };
    }

    @Test
    void updateEvent_WhenValidParams_ReturnsSuccess() throws Exception {
        // Arrange
        String updatedEventJson = createEventJson("Новая ночь кино", "2024-10-26", "Будет 10 фильмов", BigDecimal.valueOf(300), "msk");

        // Act & Assert
        mockMvc.perform(put(BASE_URL + "/" + eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEventJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Событие успешно обновлено"));
    }

    @Test
    void deleteEvent_WhenValidId_ReturnsSuccess() throws Exception {
        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/" + eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Событие успешно удалено"));
    }

    @Test
    void deleteEvent_WhenNonExistentId_ReturnsNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/9999"))
                .andExpect(status().isNotFound());
    }
}
