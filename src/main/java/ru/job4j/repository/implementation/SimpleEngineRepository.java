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
    public Engine save(Engine engine) {
        crudRepository.run(session -> session.persist(engine));
        return engine;
    }

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

    @Override
    public boolean deleteById(int id) {
        return crudRepository.runWithResult(
                "DELETE Engine WHERE id = :fId",
                Map.of("fId", id)
        );
    }

    @Override
    public boolean deleteAll() {
        return crudRepository.runWithResult(
                "DELETE FROM Engine",
                Map.of()
        );
    }
}
