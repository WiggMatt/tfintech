import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.matthew.lesson3.CustomLinkedList;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


public class CustomLinkedListTest {
    private CustomLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new CustomLinkedList<>();
    }

    @Test
    void testConstructorWithCollection() {
        list = new CustomLinkedList<>(Arrays.asList(1, 2, 3, 4, 5));

        assertEquals(5, list.size(), "Размер списка должен быть 5");

        assertEquals(1, list.get(1), "Первый элемент должен быть 1");
        assertEquals(5, list.get(5), "Пятый элемент должен быть 5");
    }

    @Test
    void testConstructorWithEmptyCollection() {
        list = new CustomLinkedList<>(Collections.emptyList());

        assertEquals(0, list.size(), "Размер списка должен быть 0");
    }

    @Test
    void testConstructorWithSingleElement() {
        list = new CustomLinkedList<>(Collections.singletonList(1));

        assertEquals(1, list.size(), "Размер списка должен быть 1");
        assertEquals(1, list.get(1), "Единственный элемент должен быть 1");
        assertEquals(1, list.getHead(), "'Голова' списка должна указывать на 1");
        assertEquals(1, list.getTail(), "'Хвост' списка должн указывать на 1");
    }

    @Test
    @DisplayName("Добавление элемента в конец списка в пустой список")
    void testAddElementToEndInEmptyList() {
        list.add(1);

        assertEquals(1, list.size(), "Размер списка должен быть равен 1 после добавления элемента.");
        assertEquals(1, list.get(1), "Добавленный элемент должен находиться в конце списка.");
        assertEquals(1, list.getHead(), "'Голова' списка должна указывать на 1");
        assertEquals(1, list.getTail(), "'Хвост' списка должн указывать на 1");
    }

    @Test
    @DisplayName("Добавление элемента в конец списка в не пустой список")
    void testAddElementToEndInNotEmptyList() {
        list.add(1);
        list.add(2);
        list.add(3);

        assertEquals(3, list.get(3));
        assertEquals(3, list.getTail());
    }

    @Test
    void testAddNullElementThrowsException() {
        assertThrows(NullPointerException.class, () -> list.add(null),
                "Должно выбрасываться NullPointerException при добавлении null");
    }

    @Test
    @DisplayName("Добавление элемента по индексу")
    void testAddElementByIndex() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(2, 4); // Добавление элемента на индекс 1

        assertEquals(4, list.size(), "Размер списка должен быть равен 4.");
        assertEquals(4, list.get(2), "Элемент 4 должен находиться на индексе 2.");
    }

    @Test
    @DisplayName("Добавление элемента по индексу")
    void testAddElementByIndexAtBeginning() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(1, 4); // Добавление элемента на индекс 1

        assertEquals(4, list.size(), "Размер списка должен быть 4 после добавления");
        assertEquals(4, list.getHead(), "Первый элемент должен быть 4");

    }

    @Test
    @DisplayName("Добавление элемента по индексу")
    void testAddElementByIndexAtEnd() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(3, 4); // Добавление элемента на индекс 1

        assertEquals(4, list.size(), "Размер списка должен быть 4 после добавления");
        assertEquals(4, list.getTail(), "Последний элемент должен быть 4");
    }

    @Test
    void testAddInvalidIndexThrowsException() {
        list.add(1);
        list.add(2);
        list.add(3);

        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 4),
                "Должно выбрасываться IndexOutOfBoundsException для отрицательного индекса");

        assertThrows(IndexOutOfBoundsException.class, () -> list.add(5, 4),
                "Должно выбрасываться IndexOutOfBoundsException, если индекс больше размера списка");
    }

    @Test
    @DisplayName("Удаление элемента по индексу")
    void testRemoveElementByIndex() {
        list.add(1);
        list.add(2);
        list.add(3);

        list.remove(1); // Удаление элемента с индексом 1

        assertEquals(2, list.size(), "Размер списка должен быть равен 2 после удаления.");
        assertEquals(3, list.get(1), "Элемент с индексом 1 теперь должен быть 3.");
    }
}
