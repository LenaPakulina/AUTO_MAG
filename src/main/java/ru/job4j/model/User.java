package ru.job4j.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "auto_users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String login;

    private String password;

    @Column(name = "user_zone")
    private String timezoneID;
}
