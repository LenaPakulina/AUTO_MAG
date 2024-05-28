package ru.job4j.repository.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Owner;
import ru.job4j.repository.OwnerRepository;
import ru.job4j.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SimpleOwnerRepository implements OwnerRepository  {
    private final CrudRepository crudRepository;

    @Override
    public Owner save(Owner owner) {
        crudRepository.run(session -> session.persist(owner));
        return owner;
    }

    @Override
    public Optional<Owner> findById(int id) {
        return crudRepository.optional("from Owner WHERE id = :fid",
                Owner.class,
                Map.of("fid", id));
    }

    @Override
    public Collection<Owner> findAll() {
        return crudRepository.query("from Owner ORDER BY id", Owner.class);
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.runWithResult(
                "DELETE Owner WHERE id = :fId",
                Map.of("fId", id)
        );
    }

    @Override
    public boolean deleteAll() {
        return crudRepository.runWithResult(
                "DELETE FROM Owner",
                Map.of()
        );
    }
}
