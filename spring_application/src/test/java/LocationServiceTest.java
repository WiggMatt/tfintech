//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.mockito.ArgumentCaptor;
//import ru.matthew.exception.ElementAlreadyExistsException;
//import ru.matthew.exception.ElementWasNotFoundException;
//import ru.matthew.dao.model.Location;
//import ru.matthew.dao.repository.InMemoryStore;
//import ru.matthew.service.LocationService;
//
//import java.util.Collections;
//import java.util.Optional;
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentCaptor.forClass;
//import static org.mockito.Mockito.*;
//
//public class LocationServiceTest {
//    private LocationService locationService;
//    private InMemoryStore<String, Location> locationStore;
//
//    @BeforeEach
//    public void setUp() {
//        locationStore = mock(InMemoryStore.class);
//        locationService = new LocationService(locationStore);
//    }
//
//    // Positive tests
//
//    @Test
//    public void getAllLocations_WhenLocationsExist_ReturnsLocations() {
//        // Arrange
//        Location location = new Location("test-slug", "Test Location");
//        when(locationStore.getAll()).thenReturn(Collections.singletonList(location));
//
//        // Act
//        var locations = locationService.getAllLocations();
//
//        // Assert
//        assertFalse(locations.isEmpty());
//        assertEquals(1, locations.size());
//        assertEquals(location, locations.iterator().next());
//    }
//
//    @Test
//    public void getLocationBySlug_WhenLocationExists_ReturnsLocation() {
//        // Arrange
//        Location location = new Location("test-slug", "Test Location");
//        when(locationStore.get("test-slug")).thenReturn(Optional.of(location));
//
//        // Act
//        Location result = locationService.getLocationBySlug("test-slug");
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(location, result);
//    }
//
//    @Test
//    public void createLocation_WhenLocationDoesNotExist_CreatesLocation() {
//        // Arrange
//        Location location = new Location("test-slug", "Test Location");
//        when(locationStore.get(location.getSlug())).thenReturn(Optional.empty());
//
//        // Act
//        locationService.createLocation(location);
//
//        // Assert
//        ArgumentCaptor<Location> captor = forClass(Location.class);
//        verify(locationStore).save(captor.capture());
//        assertEquals(location, captor.getValue());
//    }
//
//    @Test
//    public void updateLocation_WhenLocationExists_UpdatesLocation() {
//        // Arrange
//        Location location = new Location("test-slug", "Updated Location");
//        when(locationStore.get("test-slug")).thenReturn(Optional.of(location));
//
//        // Act
//        locationService.updateLocation("test-slug", location);
//
//        // Assert
//        ArgumentCaptor<Location> captor = forClass(Location.class);
//        verify(locationStore).update(captor.capture());
//        assertEquals(location, captor.getValue());
//    }
//
//    @Test
//    public void deleteLocation_WhenLocationExists_DeletesLocation() {
//        // Arrange
//        Location location = new Location("test-slug", "Updated Location");
//        when(locationStore.get("test-slug")).thenReturn(Optional.of(location));
//
//        // Act
//        locationService.deleteLocation("test-slug");
//
//        // Assert
//        verify(locationStore).delete("test-slug");
//    }
//
//    // Negative tests
//
//    @Test
//    public void getAllLocations_WhenNoLocationsExist_ThrowsException() {
//        // Arrange
//        when(locationStore.getAll()).thenReturn(Collections.emptyList());
//
//        // Act & Assert
//        assertThrows(ElementWasNotFoundException.class, () -> locationService.getAllLocations());
//    }
//
//    @Test
//    public void getLocationBySlug_WhenLocationDoesNotExist_ThrowsException() {
//        // Arrange
//        when(locationStore.get("non-existing-slug")).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(ElementWasNotFoundException.class, () -> locationService.getLocationBySlug("non-existing-slug"));
//    }
//
//    @Test
//    public void createLocation_WhenLocationAlreadyExists_ThrowsException() {
//        // Arrange
//        Location location = new Location("test-slug", "Test Location");
//        when(locationStore.get(location.getSlug())).thenReturn(Optional.of(location));
//
//        // Act & Assert
//        assertThrows(ElementAlreadyExistsException.class, () -> locationService.createLocation(location));
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideInvalidLocations")
//    public void createLocation_WhenLocationIsInvalid_ThrowsException(Location location) {
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () -> locationService.createLocation(location));
//    }
//
//    private static Stream<Location> provideInvalidLocations() {
//        return Stream.of(
//                new Location(null, "valid-slug"),
//                new Location("", "valid-slug"),
//                new Location("valid-name", null),
//                new Location("valid-name", ""),
//                new Location(null, null),
//                new Location("", "")
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideInvalidUpdateLocations")
//    public void updateLocation_WhenInvalidLocation_ThrowsException(Location location) {
//        // Arrange
//        when(locationStore.get(location.getSlug())).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(ElementWasNotFoundException.class, () -> locationService.updateLocation(location.getSlug(), location));
//    }
//
//    private static Stream<Location> provideInvalidUpdateLocations() {
//        return Stream.of(
//                new Location(null, "valid-slug"),
//                new Location("Updated Location", "valid-slug")
//        );
//    }
//
//
//    @Test
//    public void deleteLocation_WhenLocationDoesNotExist_ThrowsException() {
//        // Arrange
//        when(locationStore.get("non-existing-slug")).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(ElementWasNotFoundException.class, () -> locationService.deleteLocation("non-existing-slug"));
//    }
//}
