package ru.job4j.repository.implementation;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import ru.job4j.model.Car;
import ru.job4j.model.CarBrand;
import ru.job4j.model.Engine;
import ru.job4j.repository.EngineRepository;
import ru.job4j.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class SimpleEngineRepositoryTest {
    private static EngineRepository repository;

    @BeforeAll
    public static void initRepositories() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        repository = new SimpleEngineRepository(new CrudRepository(sf));
    }

    @AfterEach
    void clearStorage() {
        repository.deleteAll();
    }

    @DisplayName("Сохранение")
    @Test
    void whenSave() {
        Engine engine = new Engine(0, "Engine1");
        engine = repository.save(engine);
        Optional<Engine> result = repository.findById(engine.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getName()).isEqualTo(engine.getName());
    }

    @DisplayName("Проверка параметра id после сохранения элемента")
    @Test
    void whenSaveItemAndCheckItemId() {
        Engine engine = new Engine(0, "Engine1");
        engine = repository.save(engine);
        Optional<Engine> result = repository.findById(engine.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(engine);
    }

    @DisplayName("Найти все")
    @Test
    void whenTestFindAll() {
        Engine engine1 = new Engine(0, "Engine1");
        Engine engine2 = new Engine(0, "Engine2");
        engine1 = repository.save(engine1);
        engine2 = repository.save(engine2);
        Collection<Engine> expected = List.of(engine1, engine2);
        assertThat(repository.findAll()).isEqualTo(expected);
    }

    @DisplayName("Удалить по Id")
    @Test
    void whenDelete() {
        Engine engine = new Engine(0, "Engine1");
        engine = repository.save(engine);
        assertThat(repository.findAll().size()).isGreaterThan(0);
        assertThat(repository.deleteById(engine.getId())).isTrue();
        assertThat(repository.findAll().size()).isEqualTo(0);
    }
}