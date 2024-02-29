package ru.virgusman.springcourse.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.virgusman.springcourse.dao.PersonDAO;
import ru.virgusman.springcourse.models.Book;
import ru.virgusman.springcourse.models.Person;

import java.util.List;

@Controller
@RequestMapping("/people")
public class PersonController {

    private final PersonDAO personDAO;

    @Autowired
    public PersonController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    //Отображение списка всех читателей
    @GetMapping
    public String allPeople(Model model) {
        model.addAttribute("peoples", personDAO.getAllPeople());
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
        personDAO.save(person);
        return "redirect:/people";
    }

    //Переход на представление читателя
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.show(id));
        List<Book> books = personDAO.getBooks(id);
        if (!books.isEmpty()) {
            model.addAttribute("books", books);
        }
        return "people/show";
    }

    //Удаление читателя
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }

    //Переход на форму редактирования читателя
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    //Сохранений изменений читателя из формы
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";
        personDAO.update(id, person);
        return "redirect:/people";
    }
}
