package ru.job4j.repository;

import ru.job4j.model.Car;
import ru.job4j.model.Post;

import java.util.Collection;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);

    boolean deleteById(int id);

    boolean update(Post post);

    Optional<Post> findById(int id);

    Collection<Post> findAll();
}
