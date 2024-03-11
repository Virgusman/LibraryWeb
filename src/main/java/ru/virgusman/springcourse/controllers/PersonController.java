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

import java.util.List;

@Controller
@RequestMapping("/people")
public class PersonController {

    private final PersonService personService;

    private final BookService bookService;

    @Autowired
    public PersonController(PersonService personService, BookService bookService) {
        this.personService = personService;
        this.bookService = bookService;
    }

    //Отображение списка всех читателей
    @GetMapping
    public String allPeople(@RequestParam(value = "searchString", required = false, defaultValue = "") String searchString,
                            Model model) {
        if (!searchString.isEmpty()) {
            model.addAttribute("peoples", personService.searchPerson(searchString));
        } else {
            model.addAttribute("peoples", personService.findAll());
        }
        model.addAttribute("searchString", searchString);
        return "people/people";
    }

    //Отображение формы создания нового читателя
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "/people/new";
    }

    //Создание нового читателя с заполненной формы
    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "people/new";
        personService.save(person);
        return "redirect:/people";
    }

    //Переход на представление читателя
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Person person = personService.findOne(id);
        model.addAttribute("person", person);
        List<Book> books = bookService.findByReader(person);
        if (!books.isEmpty()) {
            model.addAttribute("books", books);
        }
        return "people/show";
    }

    //Удаление читателя
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personService.delete(id);
        return "redirect:/people";
    }

    //Переход на форму редактирования читателя
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.findOne(id));
        return "people/edit";
    }

    //Сохранений изменений читателя из формы
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";
        personService.update(id, person);
        return "redirect:/people";
    }
}
