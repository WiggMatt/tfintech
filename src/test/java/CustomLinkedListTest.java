import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.matthew.lesson3.CustomLinkedList;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование CustomLinkedList")
public class CustomLinkedListTest {

    @Nested
    @DisplayName("Когда список пустой")
    class WhenListIsEmpty {

        private CustomLinkedList<Integer> emptyList;

        @BeforeEach
        void setUp() {
            emptyList = new CustomLinkedList<>();
        }

        @Test
        @DisplayName("Конструктор с коллекцией элементов")
        void testConstructorWithCollection() {
            emptyList = new CustomLinkedList<>(Arrays.asList(1, 2, 3, 4, 5));

            assertEquals(5, emptyList.size(), "Размер списка должен быть 5");
            assertEquals(1, emptyList.get(0), "Первый элемент должен быть 1");
            assertEquals(5, emptyList.get(4), "Последний элемент должен быть 5");
        }

        @Test
        @DisplayName("Размер списка должен быть 0")
        void testSize() {
            assertEquals(0, emptyList.size(), "Размер пустого списка должен быть 0");
        }

        @Test
        @DisplayName("Список должен быть пустым")
        void testIsEmpty() {
            assertTrue(emptyList.isEmpty(), "Список должен быть пустым");
        }

        @Test
        @DisplayName("Добавление элемента в пустой список")
        void testAddElementToEmptyList() {
            emptyList.add(1);
            assertEquals(1, emptyList.size(), "Размер списка должен стать 1 после добавления элемента");
            assertEquals(1, emptyList.get(0), "Добавленный элемент должен быть первым в списке");
            assertEquals(1, emptyList.getHead(), "'Голова' списка должна указывать на 1");
            assertEquals(1, emptyList.getTail(), "'Хвост' списка должн указывать на 1");
        }

        @Test
        @DisplayName("Попытка удаления элемента из пустого списка должна выбросить исключение")
        void testRemoveFromEmptyListThrowsException() {
            assertThrows(IndexOutOfBoundsException.class, () -> emptyList.remove(0),
                    "Удаление элемента из пустого списка должно выбрасывать IndexOutOfBoundsException");
        }

        @Test
        @DisplayName("Метод contains для пустого списка всегда должен возвращать false")
        void testContainsOnEmptyList() {
            assertFalse(emptyList.contains(1), "Метод contains должен возвращать false для пустого списка");
        }

        @Test
        @DisplayName("Попытка доступа к элементу по индексу в пустом списке должна выбросить исключение")
        void testGetFromEmptyListThrowsException() {
            assertThrows(IndexOutOfBoundsException.class, () -> emptyList.get(0),
                    "Получение элемента из пустого списка должно выбрасывать IndexOutOfBoundsException");
        }

        @Test
        @DisplayName("Добавление всех элементов в пустой список")
        void testAddAllToEmptyList() {
            emptyList.addAll(Arrays.asList(1, 2, 3));
            assertEquals(3, emptyList.size(), "Размер списка должен быть 3 после добавления всех элементов");
            assertEquals(1, emptyList.get(0), "Первый элемент должен быть 1");
            assertEquals(2, emptyList.get(1), "Второй элемент должен быть 2");
            assertEquals(3, emptyList.get(2), "Третий элемент должен быть 3");
        }
    }

    @Nested
    @DisplayName("Когда список содержит элементы")
    class WhenListHasElements {

        private CustomLinkedList<Integer> listWithElements;

        @BeforeEach
        void setUp() {
            listWithElements = new CustomLinkedList<>(Arrays.asList(1, 2, 3));
        }

        @Test
        @DisplayName("Размер списка должен быть корректным")
        void testSize() {
            assertEquals(3, listWithElements.size(), "Размер списка должен быть 3");
        }

        @Test
        @DisplayName("Список не должен быть пустым")
        void testIsNotEmpty() {
            assertFalse(listWithElements.isEmpty(), "Список не должен быть пустым");
        }

        @Test
        @DisplayName("Добавление элемента в конец списка")
        void testAddElement() {
            listWithElements.add(4);
            assertEquals(4, listWithElements.size(), "Размер списка должен увеличиться до 4");
            assertEquals(4, listWithElements.get(3), "Последним элементом должен стать 4");
            assertEquals(4, listWithElements.getTail());
        }

        @Test
        @DisplayName("Добавление элемента по индексу")
        void testAddElementByIndex() {
            listWithElements.add(2, 4);

            assertEquals(4, listWithElements.size(), "Размер списка должен быть равен 4.");
            assertEquals(4, listWithElements.get(2), "Элемент 4 должен находиться на индексе 2");
        }

        @Test
        @DisplayName("Добавление элемента по индексу в начало списка")
        void testAddElementByIndexAtBeginning() {
            listWithElements.add(0, 4);

            assertEquals(4, listWithElements.size(), "Размер списка должен быть 4 после добавления");
            assertEquals(4, listWithElements.getHead(), "Первый элемент должен быть 4");

        }

        @Test
        @DisplayName("Добавление элемента по неверному индексу выбрасывает исключение")
        void testAddInvalidIndexThrowsException() {
            assertThrows(IndexOutOfBoundsException.class, () -> listWithElements.add(-1, 4),
                    "Должно выбрасываться IndexOutOfBoundsException для отрицательного индекса");
            assertThrows(IndexOutOfBoundsException.class, () -> listWithElements.add(5, 4),
                    "Должно выбрасываться IndexOutOfBoundsException, если индекс больше размера списка");
        }

        @Test
        @DisplayName("Добавление элемента по индексу в конец списка")
        void testAddElementByIndexAtEnd() {
            listWithElements.add(3, 4);

            assertEquals(4, listWithElements.size(), "Размер списка должен быть 4 после добавления");
            assertEquals(4, listWithElements.getTail(), "Последний элемент должен быть 4");
        }

        @Test
        @DisplayName("Удаление элемента по индексу")
        void testRemoveElementByIndex() {
            listWithElements.remove(1); // Удаляем элемент "2"
            assertEquals(2, listWithElements.size(), "Размер списка должен уменьшиться до 2");
            assertEquals(3, listWithElements.get(1), "Вторым элементом должен стать 3 после удаления");
        }

        @Test
        @DisplayName("Удаление первого элемента")
        void testRemoveFirstElement() {
            listWithElements.add(4);
            listWithElements.remove(0);

            assertEquals(3, listWithElements.size(), "Размер списка должен уменьшиться " +
                    "до 3 после удаления первого элемента");
            assertEquals(2, listWithElements.get(0), "Первым элементом должен быть 2 после удаления 1");
            assertEquals(2, listWithElements.getHead(), "'Голова' элемента должна стать 2");
        }

        @Test
        @DisplayName("Удаление последнего элемента")
        void testRemoveLastElement() {
            listWithElements.add(4);
            listWithElements.remove(3);

            assertEquals(3, listWithElements.size(), "Размер списка должен уменьшиться " +
                    "до 3 после удаления последнего элемента");
            assertEquals(3, listWithElements.get(2), "Последним элементом должен быть 3 после удаления 4");
            assertEquals(3, listWithElements.getTail(), "'Хвост' элемента должна стать 3");
        }

        @Test
        @DisplayName("Список содержит добавленный элемент")
        void testContainsElement() {
            assertTrue(listWithElements.contains(2), "Список должен содержать элемент 2");
        }

        @Test
        @DisplayName("Список не содержит элемент")
        void testDoesNotContainElement() {
            assertFalse(listWithElements.contains(5), "Список не должен содержать элемент 5");
        }

        @Test
        @DisplayName("Добавление всех элементов в непустой список")
        void testAddAllToNonEmptyList() {
            listWithElements.addAll(Arrays.asList(4, 5, 6));
            assertEquals(6, listWithElements.size(), "Размер списка должен стать 6 после добавления элементов");
            assertEquals(4, listWithElements.get(3), "Четвертым элементом должен стать 4");
            assertEquals(5, listWithElements.get(4), "Пятым элементом должен стать 5");
            assertEquals(6, listWithElements.get(5), "Шестым элементом должен стать 6");
        }

        @Test
        @DisplayName("Получение элемента по индексу")
        void testGetElementByIndex() {
            assertEquals(1, listWithElements.get(0), "Первый элемент должен быть 1");
            assertEquals(2, listWithElements.get(1), "Второй элемент должен быть 2");
            assertEquals(3, listWithElements.get(2), "Третий элемент должен быть 3");
        }

        @Test
        @DisplayName("Получение элемента по неверному индексу выбрасывает исключение")
        void testGetInvalidIndexThrowsException() {
            assertThrows(IndexOutOfBoundsException.class, () -> listWithElements.get(4),
                    "Должно выбрасываться IndexOutOfBoundsException для индекса, превышающего размер списка");
            assertThrows(IndexOutOfBoundsException.class, () -> listWithElements.get(-1),
                    "Должно выбрасываться IndexOutOfBoundsException для отрицательного индекса");
        }
    }

    @Nested
    @DisplayName("Когда список содержит один элемент")
    class WhenListHasOneElement {

        private CustomLinkedList<Integer> singleElementList;

        @BeforeEach
        void setUp() {
            singleElementList = new CustomLinkedList<>(Collections.singletonList(1));
        }

        @Test
        @DisplayName("Размер списка должен быть 1")
        void testSize() {
            assertEquals(1, singleElementList.size(), "Размер списка должен быть 1");
        }

        @Test
        @DisplayName("Удаление единственного элемента")
        void testRemoveSingleElement() {
            singleElementList.remove(0);
            assertTrue(singleElementList.isEmpty(), "Список должен стать пустым после удаления единственного элемента");
        }

        @Test
        @DisplayName("Список содержит единственный элемент")
        void testContainsSingleElement() {
            assertTrue(singleElementList.contains(1), "Список должен содержать элемент 1");
        }

        @Test
        @DisplayName("Список не содержит других элементов")
        void testDoesNotContainOtherElements() {
            assertFalse(singleElementList.contains(2), "Список не должен содержать элемент 2");
        }

        @Test
        @DisplayName("Добавление элемента в список с одним элементом")
        void testAddElementToSingleElementList() {
            singleElementList.add(2);
            assertEquals(2, singleElementList.size(), "Размер списка должен стать 2 после добавления элемента");
            assertEquals(2, singleElementList.get(1), "Вторым элементом должен стать 2");
        }
    }
}
