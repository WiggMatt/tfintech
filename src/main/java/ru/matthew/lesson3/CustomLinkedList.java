package ru.matthew.lesson3;

import java.util.Collection;

public class CustomLinkedList<E> implements MyCollection<E> {
    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    // Конструктор без параметров
    public CustomLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // Конструктор с параметром, принимающий коллекцию для инициализации
    public CustomLinkedList(Collection<? extends E> collection) {
        this();
        addAll(collection);
    }

    // Добавление элемента в конец списка
    @Override
    public void add(E data) {
        checkNullPointer(data);
        Node<E> newNode = new Node<>(data, tail, null);
        if (tail != null) {
            tail.next = newNode;
        } else {
            head = newNode;
        }
        tail = newNode;
        size++;
    }

    // Добавление элемента по указанному индексу
    @Override
    public void add(int index, E data) {
        if (index == size) {
            add(data);
        } else {
            checkNullPointer(data);
            checkIndexBound(index);
            Node<E> nodeAtIndex = getNode(index);
            Node<E> newNode = new Node<>(data, nodeAtIndex.prev, nodeAtIndex);
            if (nodeAtIndex.prev != null) {
                nodeAtIndex.prev.next = newNode;
            } else {
                head = newNode;
            }
            nodeAtIndex.prev = newNode;
            size++;
        }
    }

    // Получение элемента по индексу
    @Override
    public E get(int index) {
        checkIndexBound(index);
        return getNode(index).data;
    }

    @Override
    public boolean contains(E data) {
        if (data == null) {
            return false;
        }
        Node<E> current = head;
        while (current != null) {
            if (current.data.equals(data)) {
                return true;
            } else current = current.next;
        }
        return false;
    }

    // Удаление элемента по индексу
    @Override
    public void remove(int index) {
        checkIndexBound(index);
        Node<E> nodeToRemove = getNode(index);
        unlink(nodeToRemove);
    }

    // Добавление всех элементов из переданной коллекции в конец списка
    @Override
    public void addAll(Collection<? extends E> collection) {
        if (collection == null) {
            throw new NullPointerException("Список не может быть равен null");
        }
        for (E data : collection) {
            add(data);
        }
    }

    // Получение размера списка
    @Override
    public int size() {
        return size;
    }

    // Проверка пустой ли список
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Получение головы списка
    public E getHead() {
        return isEmpty() ? null : head.data;
    }

    // Получение хвоста списка
    public E getTail() {
        return isEmpty() ? null : tail.data;
    }

    private void unlink(Node<E> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        size--;
    }

    private Node<E> getNode(int index) {
        Node<E> current;
        if (index < size / 2) {
            current = head;
            for (int i = 1; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    // Проверка корректности индекса
    private void checkIndexBound(int index) {
        if (index <= 0 || index > size) {
            throw new IndexOutOfBoundsException("Индекс: " + index + ", Размер массива: " + size);
        }
    }

    // Проверка на null
    private void checkNullPointer(E data) {
        if (data == null) {
            throw new NullPointerException("Невозможно добавить null в список");
        }
    }
}
