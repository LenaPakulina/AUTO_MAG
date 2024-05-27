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
    public Optional<Owner> findById(int id) {
        return crudRepository.optional("from Owner WHERE id = :fid",
                Owner.class,
                Map.of("fid", id));
    }

    @Override
    public Collection<Owner> findAll() {
        return crudRepository.query("from Owner ORDER BY id", Owner.class);
    }
}
