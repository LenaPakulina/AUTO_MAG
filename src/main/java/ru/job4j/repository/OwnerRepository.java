package ru.job4j.repository;

import ru.job4j.model.Engine;
import ru.job4j.model.Owner;

import java.util.Collection;
import java.util.Optional;

public interface OwnerRepository {
    Optional<Owner> findById(int id);

    Collection<Owner> findAll();
}
