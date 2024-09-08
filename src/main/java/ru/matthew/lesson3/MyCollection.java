package ru.matthew.lesson3;

import java.util.Collection;

public interface MyCollection<E> {
    int size();
    boolean isEmpty();
    void add(E e);
    void add(int index, E element);
    void remove(int index);
    void addAll(Collection<? extends E> c);
    E get(int index);
    boolean contains(E element);
}
