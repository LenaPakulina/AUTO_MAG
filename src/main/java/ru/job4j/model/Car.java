package ru.job4j.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "cars")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String name;

    private LocalDateTime ownershipAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private CarBrand brand;

    @ManyToOne
    @JoinColumn(name = "engine_id")
    private Engine engine;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private Set<HistoryOwner> ownerHistories = new HashSet<>();
}
