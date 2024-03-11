package ru.virgusman.springcourse.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.virgusman.springcourse.models.Book;
import ru.virgusman.springcourse.models.Person;
import ru.virgusman.springcourse.services.BookService;
import ru.virgusman.springcourse.services.PersonService;
import ru.virgusman.springcourse.validators.BookValidator;

import java.util.Date;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookValidator bookValidator;
    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BookController(BookValidator bookValidator, BookService bookService, PersonService personService) {
        this.personService = personService;
        this.bookValidator = bookValidator;
        this.bookService = bookService;
    }

    //Отображение всех книг
    @GetMapping
    public String allBooks(@RequestParam(value = "sort_by_year", required = false) boolean sortByYear,
                           @RequestParam(value = "searchString", required = false, defaultValue = "") String searchString,
                           Model model) {
        if (!searchString.isEmpty()) {
            model.addAttribute("books", bookService.searchBooks(searchString));
        } else if (sortByYear) {
            model.addAttribute("books", bookService.findByYear());
            model.addAttribute("sortYear", true);
        } else {
            model.addAttribute("books", bookService.findByYearDescending());
            model.addAttribute("sortYear", false);
        }
        model.addAttribute("searchString", searchString);
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
        bookService.save(book);
        return "redirect:/books";
    }

    //Отображение представления с книгой
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model,
                       @ModelAttribute("person") Person person) {
        Book book = bookService.findOne(id);
        model.addAttribute("book", book);                           //Добавление модели книги
        if (book.getOwner() != null) {                                                  //Если внешний (Читатель) не NULL
            model.addAttribute("reader", book.getOwner());  //Добавление модели читатели из внешнего ключа
        } else {
            model.addAttribute("people", personService.findAll());             //Иначе добавление всех читателей выбора выдачи книги
        }
        return "books/show";
    }

    //Удаление книги по ID
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    //Переход на форму редактирования книги
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findOne(id));
        return "books/edit";
    }

    //Редактирование книги по заполненной форме
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "books/edit";
        bookService.update(id, book);
        return "redirect:/books";
    }

    //Выдача книги читателю
    @PatchMapping("/{id}/person")
    public String addBookToPerson(@ModelAttribute("person") Person person,
                                  @PathVariable("id") int id) {
        Book book = bookService.findOne(id);
        book.setOwner(person);
        book.setDateIssue(new Date());
        bookService.update(id, book);
        return "redirect:/books/{id}";
    }

    //Передача книги от читателя
    @PatchMapping("/{id}/free")
    public String freeBook(@PathVariable("id") int id) {
        Book book = bookService.findOne(id);
        book.setOwner(null);
        book.setDateIssue(null);
        bookService.update(id, book);
        return "redirect:/books/{id}";
    }
}
