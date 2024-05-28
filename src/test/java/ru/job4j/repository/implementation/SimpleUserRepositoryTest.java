package ru.job4j.repository.implementation;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.job4j.model.Engine;
import ru.job4j.model.User;
import ru.job4j.repository.EngineRepository;
import ru.job4j.repository.UserRepository;
import ru.job4j.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.*;

class SimpleUserRepositoryTest {
    private static UserRepository repository;

    @BeforeAll
    public static void initRepositories() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        repository = new SimpleUserRepository(new CrudRepository(sf));
    }

    @AfterEach
    void clearStorage() {
        repository.deleteAll();
    }

    @DisplayName("Сохранение")
    @Test
    void whenSave() {
        User user = new User(0, "login", "password", TimeZone.getDefault().getID());
        Optional<User> optionalUser = repository.save(user);
        assertThat(optionalUser.isPresent()).isTrue();
        user = optionalUser.get();
        Optional<User> result = repository.findByEmailAndPassword(user.getLogin(), user.getPassword());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getLogin()).isEqualTo(user.getLogin());
    }

    @DisplayName("Проверка параметра id после сохранения элемента")
    @Test
    void whenSaveItemAndCheckItemId() {
        User user = new User(0, "login", "password", TimeZone.getDefault().getID());
        Optional<User> optionalUser = repository.save(user);
        assertThat(optionalUser.isPresent()).isTrue();
        user = optionalUser.get();
        Optional<User> result = repository.findByEmailAndPassword(user.getLogin(), user.getPassword());
        assertThat(result.isPresent()).isTrue();
        assertThat(result).isEqualTo(optionalUser);
    }

    @DisplayName("Удалить по Id")
    @Test
    void whenDelete() {
        User user = new User(0, "login", "password", TimeZone.getDefault().getID());
        Optional<User> optionalUser = repository.save(user);
        assertThat(optionalUser.isPresent()).isTrue();
        user = optionalUser.get();
        assertThat(repository.deleteById(user.getId())).isTrue();
        Optional<User> result = repository.findByEmailAndPassword(user.getLogin(), user.getPassword());
        assertThat(result.isPresent()).isFalse();
    }
}