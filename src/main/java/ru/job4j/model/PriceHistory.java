package ru.job4j.model;

import javax.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode.Include;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "j_user_notification")
public class PriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;

    private long before;

    private long after;

    private LocalDateTime created;
}
