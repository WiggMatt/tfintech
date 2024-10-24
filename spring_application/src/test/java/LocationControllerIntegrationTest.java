import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.matthew.SpringApplication;
import ru.matthew.dao.model.Location;
import ru.matthew.dao.repository.LocationRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SpringApplication.class)
@AutoConfigureMockMvc
public class LocationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationRepository locationRepository;

    private static final String BASE_URL = "/api/locations";

    @BeforeEach
    void setUp() {
        Location location = new Location();
        location.setSlug("msk");
        location.setName("Москва");
        locationRepository.save(location);
    }

    @AfterEach
    void tearDown() {
        locationRepository.deleteAll();
    }

    private String createLocationJson(String slug, String name) {
        return String.format("""
                {
                    "slug": "%s",
                    "name": "%s"
                }""", slug, name);
    }

    @Test
    void getAllLocations_WhenLocationsExist_ReturnsList() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].slug").value("msk"))
                .andExpect(jsonPath("$[0].name").value("Москва"));
    }

    @ParameterizedTest
    @CsvSource({
            "'msk', 'Москва', 200",
            "'spb', 'Санкт-Петербург', 404"
    })
    void getLocationBySlug_WhenSlugProvided_ReturnsExpectedResponse(String slug, String name, int expectedStatus) throws Exception {
        // Act
        mockMvc.perform(get(BASE_URL + "/" + slug))
                .andExpect(status().is(expectedStatus))
                .andExpect(jsonPath("$.name").value(name))
                .andReturn();
    }

    @Test
    void createLocation_WhenValidLocation_ReturnsCreatedStatus() throws Exception {
        String locationJson = createLocationJson("spb", "Санкт-Петербург");

        // Act & Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Локация успешно создана"));
    }

    @ParameterizedTest
    @CsvSource({
            "'', 'Москва', 400",
            "'msk', '', 400"
    })
    void createLocation_WhenInvalidInput_ThrowsBadRequest(String slug, String name, int expectedStatus) throws Exception {
        String locationJson = createLocationJson(slug, name);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationJson))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    void updateLocation_WhenValidInput_UpdatesSuccessfully() throws Exception {
        String updatedLocationJson = createLocationJson("msk", "Новая Москва");

        mockMvc.perform(put(BASE_URL + "/msk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedLocationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Локация успешно обновлена"));
    }

    @Test
    void deleteLocation_WhenLocationExists_DeletesSuccessfully() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/msk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Локация успешно удалена"));
    }

    @Test
    void deleteLocation_WhenLocationDoesNotExist_ThrowsNotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/unknown"))
                .andExpect(status().isNotFound());
    }
}
