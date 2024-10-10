import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import ru.matthew.exception.ElementAlreadyExistsException;
import ru.matthew.exception.ElementWasNotFoundException;
import ru.matthew.model.PlaceCategory;
import ru.matthew.repository.InMemoryStore;
import ru.matthew.service.PlaceCategoryService;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

public class PlaceCategoryServiceTest {
    private PlaceCategoryService placeCategoryService;
    private InMemoryStore<Integer, PlaceCategory> placeCategoryStore;

    @BeforeEach
    public void setUp() {
        placeCategoryStore = mock(InMemoryStore.class);
        placeCategoryService = new PlaceCategoryService(placeCategoryStore);
    }

    // Positive tests

    @Test
    public void getAllPlaceCategories_WhenCategoriesExist_ReturnsCategories() {
        // Arrange
        PlaceCategory category = new PlaceCategory(1, "Test Category", "test-slug");
        when(placeCategoryStore.getAll()).thenReturn(Collections.singletonList(category));

        // Act
        var categories = placeCategoryService.getAllPlaceCategories();

        // Assert
        assertFalse(categories.isEmpty());
        assertEquals(1, categories.size());
        assertEquals(category, categories.iterator().next());
    }

    @Test
    public void getPlaceCategoryById_WhenCategoryExists_ReturnsCategory() {
        // Arrange
        PlaceCategory category = new PlaceCategory(1, "Test Category", "test-slug");
        when(placeCategoryStore.get(1)).thenReturn(Optional.of(category));

        // Act
        PlaceCategory result = placeCategoryService.getPlaceCategoryById(1);

        // Assert
        assertNotNull(result);
        assertEquals(category, result);
    }

    @Test
    public void createPlaceCategory_WhenCategoryDoesNotExist_CreatesCategory() {
        // Arrange
        PlaceCategory category = new PlaceCategory(1, "Test Category", "test-slug");
        when(placeCategoryStore.get(category.getId())).thenReturn(Optional.empty());

        // Act
        placeCategoryService.createPlaceCategory(category);

        // Assert
        ArgumentCaptor<PlaceCategory> captor = forClass(PlaceCategory.class);
        verify(placeCategoryStore).save(captor.capture());
        assertEquals(category, captor.getValue());
    }

    @Test
    public void updatePlaceCategory_WhenCategoryExists_UpdatesCategory() {
        // Arrange
        PlaceCategory category = new PlaceCategory(1, "Updated Category", "updated-slug");
        when(placeCategoryStore.get(1)).thenReturn(Optional.of(category));

        // Act
        placeCategoryService.updatePlaceCategory(1, category);

        // Assert
        ArgumentCaptor<PlaceCategory> captor = forClass(PlaceCategory.class);
        verify(placeCategoryStore).update(captor.capture());
        assertEquals(category, captor.getValue());
    }

    @Test
    public void deletePlaceCategory_WhenCategoryExists_DeletesCategory() {
        // Arrange
        when(placeCategoryStore.get(1)).thenReturn(Optional.of(new PlaceCategory(1, "Test Category", "test-slug")));

        // Act
        placeCategoryService.deletePlaceCategory(1);

        // Assert
        verify(placeCategoryStore).delete(1);
    }

    // Negative tests

    @Test
    public void getAllPlaceCategories_WhenNoCategoriesExist_ThrowsException() {
        // Arrange
        when(placeCategoryStore.getAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ElementWasNotFoundException.class, () -> placeCategoryService.getAllPlaceCategories());
    }

    @Test
    public void getPlaceCategoryById_WhenCategoryDoesNotExist_ThrowsException() {
        // Arrange
        when(placeCategoryStore.get(99)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ElementWasNotFoundException.class, () -> placeCategoryService.getPlaceCategoryById(99));
    }

    @Test
    public void createPlaceCategory_WhenCategoryAlreadyExists_ThrowsException() {
        // Arrange
        PlaceCategory category = new PlaceCategory(1, "Test Category", "test-slug");
        when(placeCategoryStore.get(category.getId())).thenReturn(Optional.of(category));

        // Act & Assert
        assertThrows(ElementAlreadyExistsException.class, () -> placeCategoryService.createPlaceCategory(category));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCategories")
    public void createPlaceCategory_WhenCategoryIsInvalid_ThrowsException(PlaceCategory category) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> placeCategoryService.createPlaceCategory(category));
    }

    private static Stream<PlaceCategory> provideInvalidCategories() {
        return Stream.of(
                new PlaceCategory(1, null, "valid-slug"),
                new PlaceCategory(1, "", "valid-slug"),
                new PlaceCategory(1, "valid-name", null),
                new PlaceCategory(1, "valid-name", ""),
                new PlaceCategory(1, null, null),
                new PlaceCategory(1, "", "")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUpdateCategories")
    public void updatePlaceCategory_WhenCategoryIsInvalid_ThrowsException(PlaceCategory category) {
        // Arrange
        when(placeCategoryStore.get(category.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ElementWasNotFoundException.class, () -> placeCategoryService.updatePlaceCategory(category.getId(), category));
    }

    private static Stream<PlaceCategory> provideInvalidUpdateCategories() {
        return Stream.of(
                new PlaceCategory(2, "Updated Category", "valid-slug"),
                new PlaceCategory(2, null, "valid-slug"),
                new PlaceCategory(2, "Updated Category", null)
        );
    }

    @Test
    public void deletePlaceCategory_WhenCategoryDoesNotExist_ThrowsException() {
        // Arrange
        when(placeCategoryStore.get(42)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ElementWasNotFoundException.class, () -> placeCategoryService.deletePlaceCategory(42));
    }
}
