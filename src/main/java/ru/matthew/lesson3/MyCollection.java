package ru.matthew.lesson3;

public interface MyCollection<E> extends Iterable<E>{
    int size();
    boolean isEmpty();
    void add(E e);
    void add(int index, E element);
    void remove(int index);
    void addAll(Iterable<? extends E> c);
    E get(int index);
    boolean contains(E element);
}
