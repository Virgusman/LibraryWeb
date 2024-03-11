package ru.virgusman.springcourse.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.virgusman.springcourse.models.Book;
import ru.virgusman.springcourse.models.Person;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByNameAndAuthorAndYear(String name, String author, int year);

    List<Book> findByOwner(Person owner);

    List<Book> findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(String name, String author);


}
