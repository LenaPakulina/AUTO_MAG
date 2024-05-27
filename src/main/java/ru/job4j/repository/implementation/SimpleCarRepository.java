package ru.job4j.repository.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Car;
import ru.job4j.repository.CarRepository;
import ru.job4j.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SimpleCarRepository implements CarRepository {
    private final CrudRepository crudRepository;

    @Override
    public Car save(Car car) {
        crudRepository.run(session -> session.persist(car));
        return car;
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.runWithResult(
                "DELETE Car WHERE id = :fId",
                Map.of("fId", id)
        );
    }

    @Override
    public boolean update(Car car) {
        return crudRepository.runFunction(session -> session.merge(car) != null);
    }

    @Override
    public Optional<Car> findById(int id) {
        return crudRepository.optional("from Car WHERE id = :fId",
                Car.class,
                Map.of("fId", id));
    }

    @Override
    public Collection<Car> findAll() {
        return crudRepository.query("from Car ORDER BY id ASC", Car.class);
    }
}
