package ru.job4j.repository;

import ru.job4j.model.Engine;
import ru.job4j.model.File;

import java.util.Collection;
import java.util.Optional;

public interface FilesRepository {
    File save(File file);

    boolean deleteAll();
}
