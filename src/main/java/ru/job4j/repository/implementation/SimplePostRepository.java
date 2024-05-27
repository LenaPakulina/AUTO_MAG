package ru.job4j.repository.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Car;
import ru.job4j.model.Post;
import ru.job4j.repository.PostRepository;
import ru.job4j.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SimplePostRepository implements PostRepository {
    private final CrudRepository crudRepository;

    @Override
    public Post save(Post post) {
        crudRepository.run(session -> session.persist(post));
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.runWithResult("DELETE Post WHERE id = :fid",
                Map.of("fid", id));
    }

    @Override
    public boolean update(Post post) {
        return crudRepository.runFunction(session -> session.merge(post) != null);
    }

    @Override
    public Optional<Post> findById(int id) {
        return crudRepository.optional("from Post WHERE id = :fId",
                Post.class,
                Map.of("fId", id));
    }

    @Override
    public Collection<Post> findAll() {
        return crudRepository.query("from Post ORDER BY id ASC", Post.class);
    }
}
