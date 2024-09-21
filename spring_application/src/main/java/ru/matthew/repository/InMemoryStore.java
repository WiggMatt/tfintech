package ru.matthew.repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.util.Optional;

public class InMemoryStore<T> {
    private final ConcurrentHashMap<Integer, T> store = new ConcurrentHashMap<>();

    public Collection<T> getAll() {
        return store.values();
    }

    public Optional<T> getById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    public void save(int id, T value) {
        store.put(id, value);
    }

    public void update(int id, T value) {
        store.replace(id, value);
    }

    public void delete(int id) {
        store.remove(id);
    }
}
