package ru.virgusman.springcourse.models;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class Book {

    private int id;
    private Integer person_id;
    @NotEmpty(message = "Поле обязательно к заполнению")
    private String name;     //Название книги
    private String author;   //Автор книги
    @Min(value = 1000, message = "Год выпуска должен быть больше 1000")
    @Digits(integer=4, fraction=0, message = "Год должен быть указан в цифровом формате (1900)")
    private int year;     //Год издания

    public Book(){

    }

    public Book(int id, Integer person_Id, String name, String author, int year) {
        this.id = id;
        this.person_id = person_Id;
        this.name = name;
        this.author = author;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Integer person_id) {
        this.person_id = person_id;
    }
}
