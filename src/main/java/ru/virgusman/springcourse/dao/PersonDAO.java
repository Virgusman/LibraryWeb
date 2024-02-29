package ru.virgusman.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.virgusman.springcourse.models.Book;
import ru.virgusman.springcourse.models.Person;

import java.util.List;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Вернуть список всех читателей
    public List<Person> getAllPeople() {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    //Сохранить читателя в БД
    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person(fullname, yearofbirth) VALUES(?, ?)",
                person.getFullName(), person.getYearOfBirth());
    }

    //Вернуть читателя по ID
    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE id=?", new Integer[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    //Удалить читателя по ID
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
    }

    //Обновить читателя по ID
    public void update(int id, Person person) {
        jdbcTemplate.update("UPDATE person SET fullname=?, yearofbirth=? WHERE id=?",
                person.getFullName(), person.getYearOfBirth(), id);
    }

    //Вернуть книги по внешнему ключу читателя
    public List<Book> getBooks(int id) {
        return jdbcTemplate.query("SELECT * FROM book where person_id=?", new Integer[]{id}, new BeanPropertyRowMapper<>(Book.class));
    }
}
