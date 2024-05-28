package ru.job4j.repository.implementation;

import lombok.AllArgsConstructor;
import ru.job4j.model.File;
import ru.job4j.repository.FilesRepository;
import ru.job4j.repository.utils.CrudRepository;

import java.util.Map;

@AllArgsConstructor
public class SimpleFilesRepository implements FilesRepository {
    private final CrudRepository crudRepository;

    @Override
    public File save(File file) {
        crudRepository.run(session -> session.persist(file));
        return file;
    }

    @Override
    public boolean deleteAll() {
        return crudRepository.runWithResult(
                "DELETE FROM File",
                Map.of()
        );
    }
}
