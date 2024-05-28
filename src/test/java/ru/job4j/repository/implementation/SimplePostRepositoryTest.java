package ru.job4j.repository.implementation;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import ru.job4j.model.*;
import ru.job4j.repository.*;
import ru.job4j.repository.utils.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.*;

class SimplePostRepositoryTest {
    private static PostRepository repository;
    private static CarRepository carRepository;
    private static UserRepository userRepository;
    private static FilesRepository filesRepository;
    private static CarBrandRepository carBrandRepository;
    private static EngineRepository engineRepository;
    private static Car lastCar;
    private static User lastUser;

    @BeforeAll
    public static void initRepositories() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        repository = new SimplePostRepository(crudRepository);
        carRepository = new SimpleCarRepository(crudRepository);
        engineRepository = new SimpleEngineRepository(crudRepository);
        carBrandRepository = new SimpleCarBrandRepository(crudRepository);
        userRepository = new SimpleUserRepository(crudRepository);
        filesRepository = new SimpleFilesRepository(crudRepository);

        Engine engine = engineRepository.save(new Engine(0, "Engine1Type"));
        CarBrand carBrand1 = carBrandRepository.save(new CarBrand(0, "Brand1"));
        lastCar = carRepository.save(
                Car.builder()
                        .name("Машина")
                        .brand(carBrand1)
                        .engine(engine)
                        .build()
        );
        User user = new User(0, "login1", "password1", TimeZone.getDefault().getID());
        lastUser = userRepository.save(user).get();
    }

    @AfterAll
    public static void clearStorage() {
        filesRepository.deleteAll();
        repository.findAll().forEach(item -> repository.deleteById(item.getId()));
        carRepository.findAll().forEach(item -> repository.deleteById(item.getId()));
        userRepository.deleteAll();
    }

    @AfterEach
    void updateStorage() {
        filesRepository.deleteAll();
        repository.findAll().forEach(item -> repository.deleteById(item.getId()));
    }

    @DisplayName("Сохранение")
    @Test
    void whenSave() {
        Post post = Post.builder().user(lastUser).car(lastCar).description("desc").build();
        post = repository.save(post);
        Optional<Post> result = repository.findById(post.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getDescription()).isEqualTo(post.getDescription());
    }

    @DisplayName("Проверка параметра id после сохранения элемента")
    @Test
    void whenSaveItemAndCheckItemId() {
        Post post = Post.builder().user(lastUser).car(lastCar).description("desc").build();
        post = repository.save(post);
        Optional<Post> result = repository.findById(post.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(post);
    }

    @DisplayName("Обновление параметра")
    @Test
    void whenUpdateThenCheckName() {
        Post post = Post.builder().user(lastUser).car(lastCar).description("desc").build();
        post = repository.save(post);
        post.setDescription("UpdateDesc");
        assertThat(repository.update(post)).isTrue();
        Optional<Post> result = repository.findById(post.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getDescription()).isEqualTo(post.getDescription());
    }

    @DisplayName("Найти все")
    @Test
    void whenTestFindAll() {
        Post post1 = Post.builder().user(lastUser).car(lastCar).description("desc1").build();
        Post post2 = Post.builder().user(lastUser).car(lastCar).description("desc2").build();
        post1 = repository.save(post1);
        post2 = repository.save(post2);
        Collection<Post> expected = List.of(post1, post2);
        assertThat(repository.findAll()).isEqualTo(expected);
    }

    @DisplayName("Удалить по Id")
    @Test
    void whenDelete() {
        Post post = Post.builder().user(lastUser).car(lastCar).description("desc1").build();
        post = repository.save(post);
        assertThat(repository.findAll().size()).isGreaterThan(0);
        assertThat(repository.deleteById(post.getId())).isTrue();
        assertThat(repository.findAll().size()).isEqualTo(0);
    }

    @DisplayName("Найти посты с фотографиями")
    @Test
    void whenFindPostsWithPhoto() {
        filesRepository.deleteAll();
        Post post1 = Post.builder().user(lastUser).car(lastCar).description("desc1").build();
        Post post2 = Post.builder().user(lastUser).car(lastCar).description("desc2").build();
        Post post3 = Post.builder().user(lastUser).car(lastCar).description("desc3").build();
        post1 = repository.save(post1);
        post2 = repository.save(post2);
        post3 = repository.save(post3);
        File file1 = new File(0, "name", "path1", post1.getId());
        File file2 = new File(0, "name", "path2", post1.getId());
        File file3 = new File(0, "name", "path3", post3.getId());
        file1 = filesRepository.save(file1);
        file2 = filesRepository.save(file2);
        file3 = filesRepository.save(file3);
        Collection<Post> result = repository.findPostsWithPhoto();
        Collection<Post> expected = List.of(post1, post3);
        assertThat(result.size()).isEqualTo(expected.size());
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Найти посты с машиной марки = ?")
    @Test
    void whenFindPostsOfCarBrand() {
        Engine engine = engineRepository.save(new Engine(0, "Engine1Type"));
        CarBrand carBrand1 = carBrandRepository.save(new CarBrand(0, "Brand1"));
        CarBrand carBrand2 = carBrandRepository.save(new CarBrand(0, "Brand2"));
        CarBrand carBrand3 = carBrandRepository.save(new CarBrand(0, "Brand3"));
        Car car1 = carRepository.save(Car.builder().name("Машина").brand(carBrand1).engine(engine).build());
        Car car2 = carRepository.save(Car.builder().name("Машина").brand(carBrand2).engine(engine).build());
        Car car3 = carRepository.save(Car.builder().name("Машина").brand(carBrand3).engine(engine).build());
        Car car4 = carRepository.save(Car.builder().name("Машина").brand(carBrand1).engine(engine).build());
        Post post1 = Post.builder().user(lastUser).car(car1).description("desc1").build();
        Post post2 = Post.builder().user(lastUser).car(car2).description("desc2").build();
        Post post3 = Post.builder().user(lastUser).car(car3).description("desc3").build();
        Post post4 = Post.builder().user(lastUser).car(car4).description("desc4").build();
        post1 = repository.save(post1);
        post2 = repository.save(post2);
        post3 = repository.save(post3);
        post4 = repository.save(post4);
        Collection<Post> result = repository.findPostsOfCarBrand(carBrand1.getId());
        Collection<Post> expected = List.of(post1, post4);
        assertThat(result.size()).isEqualTo(expected.size());
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Найти посты за сегодня")
    @Test
    void whenFindPostsToday() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = LocalDateTime.now().minusDays(2);
        LocalDateTime lastWeek = LocalDateTime.now().minusWeeks(1);
        Post post1 = Post.builder().created(today).user(lastUser).car(lastCar).description("desc1").build();
        Post post2 = Post.builder().created(yesterday).user(lastUser).car(lastCar).description("desc1").build();
        Post post3 = Post.builder().created(today).user(lastUser).car(lastCar).description("desc1").build();
        Post post4 = Post.builder().created(lastWeek).user(lastUser).car(lastCar).description("desc1").build();
        post1 = repository.save(post1);
        post2 = repository.save(post2);
        post3 = repository.save(post3);
        post4 = repository.save(post4);
        Collection<Post> result = repository.findPostForDate(LocalDateTime.now().minusDays(1), LocalDateTime.now());
        Collection<Post> expected = List.of(post1, post3);
        assertThat(result.size()).isEqualTo(expected.size());
        assertThat(result).isEqualTo(expected);
    }
}