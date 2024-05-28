package ru.job4j.repository.implementation;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import ru.job4j.model.CarBrand;
import ru.job4j.model.Owner;
import ru.job4j.model.Owner;
import ru.job4j.model.User;
import ru.job4j.repository.CarBrandRepository;
import ru.job4j.repository.OwnerRepository;
import ru.job4j.repository.OwnerRepository;
import ru.job4j.repository.UserRepository;
import ru.job4j.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.*;

class SimpleOwnerRepositoryTest {
    private static OwnerRepository repository;
    private static UserRepository userRepository;
    private static User lastUser;

    @BeforeAll
    public static void initRepositories() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        repository = new SimpleOwnerRepository(crudRepository);
        userRepository = new SimpleUserRepository(crudRepository);
    }

    @BeforeEach
    void updateStorage() {
        User user = new User(0, "login", "password", TimeZone.getDefault().getID());
        Optional<User> result = userRepository.save(user);
        result.ifPresent(value -> lastUser = value);
    }

    @AfterEach
    void clearStorage() {
        repository.findAll().forEach(item -> repository.deleteById(item.getId()));
        userRepository.deleteAll();
        lastUser = null;
    }

    @DisplayName("Сохранение")
    @Test
    void whenSave() {
        Owner owner = new Owner(0, "Owner1", lastUser);
        owner = repository.save(owner);
        Optional<Owner> result = repository.findById(owner.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getName()).isEqualTo(owner.getName());
    }

    @DisplayName("Проверка параметра id после сохранения элемента")
    @Test
    void whenSaveItemAndCheckItemId() {
        Owner owner = new Owner(0, "Owner1", lastUser);
        owner = repository.save(owner);
        Optional<Owner> result = repository.findById(owner.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(owner);
    }

    @DisplayName("Найти все")
    @Test
    void whenTestFindAll() {
        Owner owner1 = new Owner(0, "Owner1", lastUser);
        Owner owner2 = new Owner(0, "Owner2", lastUser);
        owner1 = repository.save(owner1);
        owner2 = repository.save(owner2);
        Collection<Owner> expected = List.of(owner1, owner2);
        assertThat(repository.findAll()).isEqualTo(expected);
    }

    @DisplayName("Удалить по Id")
    @Test
    void whenDelete() {
        Owner owner = new Owner(0, "Owner1", lastUser);
        owner = repository.save(owner);
        assertThat(repository.findAll().size()).isGreaterThan(0);
        assertThat(repository.deleteById(owner.getId())).isTrue();
        assertThat(repository.findAll().size()).isEqualTo(0);
    }
}