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
import ru.job4j.repository.CarBrandRepository;
import ru.job4j.repository.CarRepository;
import ru.job4j.repository.EngineRepository;
import ru.job4j.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@AllArgsConstructor
class SimpleCarRepositoryTest {
    private static CarRepository repository;
    private static EngineRepository engineRepository;
    private static CarBrandRepository carBrandRepository;
    private static Engine engine;
    private static CarBrand carBrand;

    @BeforeAll
    public static void initRepositories() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        repository = new SimpleCarRepository(crudRepository);
        engineRepository = new SimpleEngineRepository(crudRepository);
        carBrandRepository = new SimpleCarBrandRepository(crudRepository);
    }

    @BeforeEach
    void updateStorage() {
        engine = engineRepository.save(new Engine(0, "Engine1Type"));
        carBrand = carBrandRepository.save(new CarBrand(0, "Brand2"));
    }

    @AfterEach
    void clearStorage() {
        repository.findAll().forEach(item -> repository.deleteById(item.getId()));
        engineRepository.deleteAll();
        carBrandRepository.deleteAll();
        engine = null;
        carBrand = null;
    }

    @DisplayName("Сохранение")
    @Test
    void whenSave() {
        Car car = Car.builder()
                .name("Ласточка 3000")
                .brand(carBrand)
                .engine(engine)
                .build();
        car = repository.save(car);
        Optional<Car> result = repository.findById(car.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getName()).isEqualTo(car.getName());
    }

    @DisplayName("Проверка параметра id после сохранения элемента")
    @Test
    void whenSaveItemAndCheckItemId() {
        Car car = createCar("Ласточка 3000");
        car = repository.save(car);
        Optional<Car> result = repository.findById(car.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(car);
    }

    @DisplayName("Обновление параметра")
    @Test
    void whenUpdateThenCheckName() {
        Car car = createCar("Ласточка 3000");
        car = repository.save(car);
        car.setName("Ласточка 5000");
        repository.update(car);
        Optional<Car> result = repository.findById(car.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getName()).isEqualTo(car.getName());
    }

    @DisplayName("Найти все")
    @Test
    void whenTestFindAll() {
        Car car1 = createCar("Ласточка 3000");
        Car car2 = createCar("Нива");
        car1 = repository.save(car1);
        car2 = repository.save(car2);
        Collection<Car> expected = List.of(car1, car2);
        assertThat(repository.findAll()).isEqualTo(expected);
    }

    @DisplayName("Удалить по Id")
    @Test
    void whenDelete() {
        Car car = createCar("Ласточка 3000");
        car = repository.save(car);
        assertThat(repository.findAll().size()).isGreaterThan(0);
        assertThat(repository.deleteById(car.getId())).isTrue();
        assertThat(repository.findAll().size()).isEqualTo(0);
    }

    private Car createCar(String name) {
        return Car.builder()
                .name(name)
                .brand(carBrand)
                .engine(engine)
                .build();
    }
}