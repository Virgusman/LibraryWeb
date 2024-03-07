package ru.virgusman.springcourse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
@Table(name = "Person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Поле обязательно к заполнению")
    @Pattern(regexp = "([А-ЯЁ][а-яА-ЯёЁ\\-]+\\s){2}[А-ЯЁ][а-яё]+",
            message = "Должен быть формат (Фамилия Имя Отчество) на русском языке")
    @Column(name = "fullname")
    private String fullName;  //ФИО
    @Min(value = 1900, message = "Год рождения должен быть больше 1900")
    @Digits(integer = 4, fraction = 0, message = "Год должен быть указан в цифровом формате (1900)")
    @Column(name = "yearofbirth")
    private int yearOfBirth;  //Год рождения

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person() {

    }

    public Person(int id, String fullName, int yearOfBirth) {
        this.id = id;
        this.fullName = fullName;
        this.yearOfBirth = yearOfBirth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
