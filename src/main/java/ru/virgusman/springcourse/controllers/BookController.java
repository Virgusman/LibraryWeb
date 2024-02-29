package ru.virgusman.springcourse.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.virgusman.springcourse.dao.BookDAO;
import ru.virgusman.springcourse.dao.PersonDAO;
import ru.virgusman.springcourse.models.Book;
import ru.virgusman.springcourse.models.Person;
import ru.virgusman.springcourse.validators.BookValidator;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;
    private final BookValidator bookValidator;

    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO, BookValidator bookValidator) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
        this.bookValidator = bookValidator;
    }

    //Отображение всех книг
    @GetMapping
    public String allBooks(Model model) {
        model.addAttribute("books", bookDAO.getAllBook());
        return "books/books";
    }

    //Отображение формы создания новой книги
    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "/books/new";
    }

    //Создание новой книги, запись в БД с заполненной формы
    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "books/new";
        bookDAO.save(book);
        return "redirect:/books";
    }

    //Отображение представления с книгой
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model,
                       @ModelAttribute("person") Person person){
        Book book = bookDAO.show(id);
        model.addAttribute("book", bookDAO.show(id));                           //Добавление модели книги
        if (book.getPerson_id() != null) {                                                  //Если внешний (Читатель) не NULL
            model.addAttribute("reader", personDAO.show(book.getPerson_id()));  //Добавление модели читатели из внешнего ключа
        } else {
            model.addAttribute("people", personDAO.getAllPeople());             //Иначе добавление всех читателей выбора выдачи книги
        }
        return "books/show";
    }

    //Удаление книги по ID
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }

    //Переход на форму редактирования книги
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDAO.show(id));
        return "books/edit";
    }

    //Редактирование книги по заполненной форме
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "books/edit";
        bookDAO.update(id, book);
        return "redirect:/books";
    }

    //Выдача книги читателю
    @PatchMapping("/{id}/person")
    public String addBookToPerson(@ModelAttribute("person") Person person,
                                  @PathVariable("id") int id) {
        Book book = bookDAO.show(id);
        book.setPerson_id(person.getId());
        bookDAO.update(id, book);
        return "redirect:/books/{id}";
    }

    //Передача книги от читателя
    @PatchMapping("/{id}/free")
    public String freeBook(@PathVariable("id") int id) {
        bookDAO.free(id);
        return "redirect:/books/{id}";
    }
}
