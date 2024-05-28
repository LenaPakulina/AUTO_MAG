package ru.job4j.repository.implementation;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.job4j.model.CarBrand;
import ru.job4j.model.Engine;
import ru.job4j.repository.CarBrandRepository;
import ru.job4j.repository.EngineRepository;
import ru.job4j.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class SimpleCarBrandRepositoryTest {
    private static CarBrandRepository repository;

    @BeforeAll
    public static void initRepositories() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        repository = new SimpleCarBrandRepository(new CrudRepository(sf));
    }

    @AfterEach
    void clearStorage() {
        repository.deleteAll();
    }

    @DisplayName("Сохранение")
    @Test
    void whenSave() {
        CarBrand carBrand = new CarBrand(0, "Lada");
        carBrand = repository.save(carBrand);
        Optional<CarBrand> result = repository.findById(carBrand.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getName()).isEqualTo(carBrand.getName());
    }

    @DisplayName("Проверка параметра id после сохранения элемента")
    @Test
    void whenSaveItemAndCheckItemId() {
        CarBrand carBrand = new CarBrand(0, "Lada");
        carBrand = repository.save(carBrand);
        Optional<CarBrand> result = repository.findById(carBrand.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(carBrand);
    }

    @DisplayName("Найти все")
    @Test
    void whenTestFindAll() {
        CarBrand carBrand1 = new CarBrand(0, "Lada1");
        carBrand1 = repository.save(carBrand1);
        CarBrand carBrand2 = new CarBrand(0, "Lada2");
        carBrand2 = repository.save(carBrand2);
        Collection<CarBrand> expected = List.of(carBrand1, carBrand2);
        assertThat(repository.findAll()).isEqualTo(expected);
    }

    @DisplayName("Удалить по Id")
    @Test
    void whenDelete() {
        CarBrand carBrand = new CarBrand(0, "Lada");
        carBrand = repository.save(carBrand);
        assertThat(repository.findAll().size()).isGreaterThan(0);
        assertThat(repository.deleteById(carBrand.getId())).isTrue();
        assertThat(repository.findAll().size()).isEqualTo(0);
    }
}