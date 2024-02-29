package ru.virgusman.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.virgusman.springcourse.models.Book;

import java.util.List;


@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Вернуть все книги
    public List<Book> getAllBook() {
        return jdbcTemplate.query("SELECT * FROM book", new BeanPropertyRowMapper<>(Book.class));
    }

    //Сохранение книги в БД
    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO book(name, author, year) VALUES(?, ?, ?)",
                book.getName(), book.getAuthor(), book.getYear());
    }

    //Определение наличия книги в БД
    public int countBooks(Book book) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM book b WHERE b.name=? and b.author=? and b.year=?",
                new Object[]{book.getName(), book.getAuthor(), book.getYear()}, Integer.class);
    }

    //Вернуть книгу по ID
    public Book show(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE id=?", new Integer[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    //Удаление книги из БД
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }

    //Обновление книги в БД по ID
    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE book SET person_id=?, name=?, author=?, year=? WHERE id=?",
                book.getPerson_id(), book.getName(), book.getAuthor(), book.getYear(), id);
    }

    //Удаление связи с читателем
    public void free(int id) {
        jdbcTemplate.update("UPDATE Book set person_id = NULL where id = ?", id);
    }
}
