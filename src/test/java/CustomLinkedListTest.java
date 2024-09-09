import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.matthew.lesson3.CustomLinkedList;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


public class CustomLinkedListTest {
    private CustomLinkedList<Integer> notEmptyList;
    private CustomLinkedList<Integer> emptyList;


    @BeforeEach
    void setUp() {
        notEmptyList = new CustomLinkedList<>(Arrays.asList(1, 2, 3));
        emptyList = new CustomLinkedList<>();
    }

    @Test
    @DisplayName("Конструктор с коллекцией элементов")
    void testConstructorWithCollection() {
        emptyList = new CustomLinkedList<>(Arrays.asList(1, 2, 3, 4, 5));

        assertEquals(5, emptyList.size(), "Размер списка должен быть 5");
        assertEquals(1, emptyList.get(1), "Первый элемент должен быть 1");
        assertEquals(5, emptyList.get(5), "Пятый элемент должен быть 5");
    }

    @Test
    @DisplayName("Конструктор с пустой коллекцией")
    void testConstructorWithEmptyCollection() {
        emptyList = new CustomLinkedList<>(Collections.emptyList());

        assertEquals(0, emptyList.size(), "Размер списка должен быть 0");
    }

    @Test
    @DisplayName("Конструктор с одним элементом")
    void testConstructorWithSingleElement() {
        emptyList = new CustomLinkedList<>(Collections.singletonList(1));

        assertEquals(1, emptyList.size(), "Размер списка должен быть 1");
        assertEquals(1, emptyList.get(1), "Единственный элемент должен быть 1");
        assertEquals(1, emptyList.getHead(), "'Голова' списка должна указывать на 1");
        assertEquals(1, emptyList.getTail(), "'Хвост' списка должн указывать на 1");
    }

    @Test
    @DisplayName("Добавление элемента в конец списка в пустой список")
    void testAddElementToEndInEmptyList() {
        emptyList.add(1);

        assertEquals(1, emptyList.size(), "Размер списка должен быть равен 1");
        assertEquals(1, emptyList.get(1), "Добавленный элемент должен находиться в конце списка");
        assertEquals(1, emptyList.getHead(), "'Голова' списка должна указывать на 1");
        assertEquals(1, emptyList.getTail(), "'Хвост' списка должн указывать на 1");
    }

    @Test
    @DisplayName("Добавление элемента в конец списка в не пустой список")
    void testAddElementToEndInNotEmptyList() {
        assertEquals(3, notEmptyList.get(3));
        assertEquals(3, notEmptyList.getTail());
    }

    @Test
    @DisplayName("Добавление null элемента выбрасывает исключение")
    void testAddNullElementThrowsException() {
        assertThrows(NullPointerException.class, () -> emptyList.add(null),
                "Должно выбрасываться NullPointerException при добавлении null");
    }

    @Test
    @DisplayName("Добавление элемента по индексу")
    void testAddElementByIndex() {
        notEmptyList.add(2, 4);

        assertEquals(4, notEmptyList.size(), "Размер списка должен быть равен 4.");
        assertEquals(4, notEmptyList.get(2), "Элемент 4 должен находиться на индексе 2.");
    }

    @Test
    @DisplayName("Добавление элемента по индексу в начало списка")
    void testAddElementByIndexAtBeginning() {
        notEmptyList.add(1, 4);

        assertEquals(4, notEmptyList.size(), "Размер списка должен быть 4 после добавления");
        assertEquals(4, notEmptyList.getHead(), "Первый элемент должен быть 4");

    }

    @Test
    @DisplayName("Добавление элемента по индексу в конец списка")
    void testAddElementByIndexAtEnd() {
        notEmptyList.add(3, 4);

        assertEquals(4, notEmptyList.size(), "Размер списка должен быть 4 после добавления");
        assertEquals(4, notEmptyList.getTail(), "Последний элемент должен быть 4");
    }

    @Test
    @DisplayName("Добавление элемента по неверному индексу выбрасывает исключение")
    void testAddInvalidIndexThrowsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> notEmptyList.add(-1, 4),
                "Должно выбрасываться IndexOutOfBoundsException для отрицательного индекса");
        assertThrows(IndexOutOfBoundsException.class, () -> notEmptyList.add(5, 4),
                "Должно выбрасываться IndexOutOfBoundsException, если индекс больше размера списка");
    }

    @Test
    @DisplayName("Получение элемента по неверному индексу выбрасывает исключение")
    void testGetInvalidIndexThrowsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> notEmptyList.get(0),
                "Должно выбрасываться IndexOutOfBoundsException для индекса 0");
        assertThrows(IndexOutOfBoundsException.class, () -> notEmptyList.get(4),
                "Должно выбрасываться IndexOutOfBoundsException для индекса, превышающего размер списка");
        assertThrows(IndexOutOfBoundsException.class, () -> notEmptyList.get(-1),
                "Должно выбрасываться IndexOutOfBoundsException для отрицательного индекса");
    }

    @Test
    @DisplayName("Получение элемента из пустого списка выбрасывает исключение")
    void testGetFromEmptyListThrowsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> emptyList.get(1),
                "Должно выбрасываться IndexOutOfBoundsException при попытке получить элемент из пустого списка");
    }

    @Test
    @DisplayName("Проверка получения эдементов из середины списка")
    void testGetNodeFirstHalf() {
        notEmptyList.add(4);
        assertEquals(1, notEmptyList.get(1));
        assertEquals(3, notEmptyList.get(3));
    }

    @Test
    @DisplayName("Список содержит элемент")
    void testContainsElementExists() {
        assertTrue(notEmptyList.contains(2), "Список должен содержать элемент 2");
        assertTrue(notEmptyList.contains(1), "Список должен содержать элемент 1");
        assertTrue(notEmptyList.contains(3), "Список должен содержать элемент 3");
    }

    @Test
    @DisplayName("Список не содержит элемент")
    void testContainsElementNotExists() {
        assertFalse(notEmptyList.contains(6), "Список не должен содержать элемент 6");
        assertFalse(notEmptyList.contains(0), "Список не должен содержать элемент 0");
    }

    @Test
    @DisplayName("Поиск null элемента выбрасывает исключение")
    void testContainsNullElementThrowsException() {
        assertThrows(NullPointerException.class, () -> notEmptyList.contains(null),
                "Ожидается NullPointerException при поиске null");
    }

    @Test
    @DisplayName("Проверка метода contains с строками")
    void testContainsWithStrings() {
        CustomLinkedList<String> list = new CustomLinkedList<>(Arrays.asList("apple", "banana", "cherry"));

        assertTrue(list.contains("banana"), "Список должен содержать элемент 'banana'");
        assertFalse(list.contains("grape"), "Список не должен содержать элемент 'grape'");
    }

    @Test
    @DisplayName("Удаление первого элемента")
    void testRemoveFirstElement() {
        notEmptyList.add(4);
        notEmptyList.remove(1);

        assertEquals(3, notEmptyList.size(), "Размер списка должен уменьшиться " +
                "до 3 после удаления первого элемента");
        assertEquals(2, notEmptyList.get(1), "Первым элементом должен быть 2 после удаления 1");
        assertEquals(2, notEmptyList.getHead(), "'Голова' элемента должна стать 2");
    }

    @Test
    @DisplayName("Удаление последнего элемента")
    void testRemoveLastElement() {
        notEmptyList.add(4);
        notEmptyList.remove(4);

        assertEquals(3, notEmptyList.size(), "Размер списка должен уменьшиться " +
                "до 3 после удаления последнего элемента");
        assertEquals(3, notEmptyList.get(3), "Последним элементом должен быть 3 после удаления 4");
        assertEquals(3, notEmptyList.getTail(), "'Хвост' элемента должна стать 3");
    }

    @Test
    @DisplayName("Удаление элемента в середине списка")
    void testRemoveMiddleElement() {
        notEmptyList.add(4);
        notEmptyList.remove(2);

        assertEquals(3, notEmptyList.size(), "Размер списка должен уменьшиться " +
                "до 3 после удаления последнего элемента");
        assertEquals(3, notEmptyList.get(2), "Вторым элементом должен остаться 3");
        assertEquals(4, notEmptyList.get(3), "Третьим элементом должен стать 40 после удаления 30");
    }

    @Test
    @DisplayName("Удаление несуществующих элементов")
    void testRemoveInvalidIndexThrowsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> notEmptyList.remove(0),
                "Должно выбрасываться IndexOutOfBoundsException для индекса 0");
        assertThrows(IndexOutOfBoundsException.class, () -> notEmptyList.remove(4),
                "Должно выбрасываться IndexOutOfBoundsException для индекса, превышающего размер списка");
        assertThrows(IndexOutOfBoundsException.class, () -> notEmptyList.remove(-1),
                "Должно выбрасываться IndexOutOfBoundsException для отрицательного индекса");
    }

    @Test
    @DisplayName("Добавление коллекции в пустой список")
    void testAddAllToEmptyList() {
        emptyList.addAll(Arrays.asList(1, 2, 3, 4, 5));

        assertEquals(5, emptyList.size(), "Размер списка должен быть 5 после добавления 5 элементов");
        assertEquals(1, emptyList.get(1), "Первый элемент должен быть 1");
        assertEquals(5, emptyList.get(5), "Последний элемент должен быть 5");
    }

    @Test
    @DisplayName("Добавление коллекции в не пустой список")
    void testAddAllToNonEmptyList() {
        notEmptyList.addAll(Arrays.asList(4, 5, 6));

        assertEquals(6, notEmptyList.size(), "Размер списка должен быть 6 " +
                "после добавления 3 элементов");
        assertEquals(1, notEmptyList.get(1), "Первый элемент должен быть 1");
        assertEquals(6, notEmptyList.get(6), "Последний элемент должен быть 6");
    }

    @Test
    @DisplayName("Добавление пустой коллекции в пустой список")
    void testAddEmptyCollection() {
        notEmptyList.addAll(Collections.emptyList());

        assertEquals(3, notEmptyList.size(), "Размер списка должен остаться " +
                "3 после добавления пустой коллекции");
        assertEquals(1, notEmptyList.get(1), "Первый элемент должен быть 1");
        assertEquals(3, notEmptyList.get(3), "Последний элемент должен быть 3");
    }

    @Test
    @DisplayName("Добавление null в пустой список")
    void testAddAllWithNullCollectionThrowsException() {
        assertThrows(NullPointerException.class, () -> notEmptyList.addAll(null),
                "Ожидалось NullPointerException при добавлении null коллекции");
    }

    @Test
    @DisplayName("Проверка размера пустого списка")
    void testSizeForEmptyList() {
        assertEquals(0, emptyList.size(), "Размер пустого списка должен быть 0");
    }

    @Test
    @DisplayName("Проверка размера не пустого списка")
    void testSizeForNonEmptyList() {
        assertEquals(3, notEmptyList.size(), "Размер списка должен быть 3 после инициализации");
    }

    @Test
    @DisplayName("Проверка isEmpty на пустом списке")
    void testIsEmptyForEmptyList() {
        assertTrue(emptyList.isEmpty(), "Пустой список должен вернуть true");
    }

    @Test
    @DisplayName("Проверка isEmpty на не пустом списке")
    void testIsEmptyForNonEmptyList() {
        assertFalse(notEmptyList.isEmpty(), "Непустой список должен вернуть false");
    }

    @Test
    @DisplayName("Проверка isEmpty на очищенном списке")
    void testIsEmptyAfterClearing() {
        notEmptyList.remove(1);
        notEmptyList.remove(1);
        notEmptyList.remove(1);

        assertTrue(notEmptyList.isEmpty(), "Список должен быть пуст после удаления всех элементов");
    }
}
