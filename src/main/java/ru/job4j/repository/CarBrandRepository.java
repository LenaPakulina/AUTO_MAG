package ru.job4j.repository;

import ru.job4j.model.Car;
import ru.job4j.model.CarBrand;
import ru.job4j.model.Engine;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface CarBrandRepository {
    CarBrand save(CarBrand carBrand);

    boolean deleteById(int id);

    boolean deleteAll();

    Collection<CarBrand> findAll();

    Optional<CarBrand> findById(int id);
}
