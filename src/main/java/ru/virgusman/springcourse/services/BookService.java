package ru.virgusman.springcourse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virgusman.springcourse.models.Person;
import ru.virgusman.springcourse.repositories.BookRepository;
import ru.virgusman.springcourse.models.Book;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    public Book findOne(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setId(id);
        bookRepository.save(book);
    }

    public List<Book> countBooks(Book book) {
        return bookRepository.findByNameAndAuthorAndYear(book.getName(), book.getAuthor(), book.getYear());
    }

    public List<Book> findByReader(Person person) {
        return bookRepository.findByOwner(person);
    }

    public List<Book> findByYear() {
        return bookRepository.findAll(Sort.by("year"));
    }

    public List<Book> findByYearDescending() {
        return bookRepository.findAll(Sort.by("year").descending());
    }

    public List<Book> searchBooks(String searchString) {
        return bookRepository.findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(searchString, searchString);
    }

}
