package ru.matthew.repository;

import ru.matthew.model.Identifiable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.util.Optional;

public class InMemoryStore<K, T extends Identifiable<K>> {
    private final ConcurrentHashMap<K, T> store = new ConcurrentHashMap<>();

    public Collection<T> getAll() {
        return store.values();
    }

    public Optional<T> get(K key) {
        return Optional.ofNullable(store.get(key));
    }

    public void save(T value) {
        if (value != null) {
            K key = value.getKey();
            store.put(key, value);
        } else {
            throw new IllegalArgumentException("Value must implement Identifiable interface");
        }
    }

    public void update(T value) {
        if (value != null) {
            K key = value.getKey();
            store.replace(key, value);
        } else {
            throw new IllegalArgumentException("Value must implement Identifiable interface");
        }
    }

    public void delete(K key) {
        store.remove(key);
    }
}
