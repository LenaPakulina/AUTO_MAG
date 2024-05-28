package ru.job4j.repository.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.CarBrand;
import ru.job4j.model.Engine;
import ru.job4j.repository.CarBrandRepository;
import ru.job4j.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SimpleCarBrandRepository implements CarBrandRepository {
    private final CrudRepository crudRepository;

    @Override
    public CarBrand save(CarBrand carBrand) {
        crudRepository.run(session -> session.persist(carBrand));
        return carBrand;
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.runWithResult(
                "DELETE CarBrand WHERE id = :fId",
                Map.of("fId", id)
        );
    }

    @Override
    public boolean deleteAll() {
        return crudRepository.runWithResult(
                "DELETE FROM CarBrand",
                Map.of()
        );
    }

    @Override
    public Collection<CarBrand> findAll() {
        return crudRepository.query("from CarBrand ORDER BY id ASC", CarBrand.class);
    }

    @Override
    public Optional<CarBrand> findById(int id) {
        return crudRepository.optional("from CarBrand WHERE id = :fId",
                CarBrand.class,
                Map.of("fId", id));
    }
}
