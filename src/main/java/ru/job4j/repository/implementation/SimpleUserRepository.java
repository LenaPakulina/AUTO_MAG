package ru.job4j.repository.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.model.User;
import ru.job4j.repository.utils.CrudRepository;
import ru.job4j.repository.UserRepository;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class SimpleUserRepository implements UserRepository {
    private final CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        try {
            crudRepository.run(session -> session.persist(user));
            return Optional.of(user);
        } catch (Exception e) {
            log.error("Failed to save user.");
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return crudRepository.optional(
                "from User where login = :fLogin AND password = :fPassword", User.class,
                Map.of(
                        "fLogin", email,
                        "fPassword", password
                )
        );
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.runWithResult(
                "DELETE User WHERE id = :fId",
                Map.of("fId", id)
        );
    }

    @Override
    public boolean deleteAll() {
        return crudRepository.runWithResult(
                "DELETE FROM User",
                Map.of()
        );
    }
}
