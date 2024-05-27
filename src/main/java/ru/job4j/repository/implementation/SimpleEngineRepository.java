package ru.job4j.repository.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Car;
import ru.job4j.model.Engine;
import ru.job4j.repository.EngineRepository;
import ru.job4j.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SimpleEngineRepository implements EngineRepository {
    private final CrudRepository crudRepository;

    @Override
    public Optional<Engine> findById(int id) {
        return crudRepository.optional("from Engine WHERE id = :fId",
                Engine.class,
                Map.of("fId", id));
    }

    @Override
    public Collection<Engine> findAll() {
        return crudRepository.query("from Engine ORDER BY id ASC", Engine.class);
    }
}
