package ru.virgusman.springcourse.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.virgusman.springcourse.models.Book;
import ru.virgusman.springcourse.services.BookService;

@Component
public class BookValidator implements Validator {
    private final BookService bookService;

    public BookValidator(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    //Поиск идентичной книги в БД
    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;
        if (!bookService.countBooks(book).isEmpty()){
            errors.rejectValue("name", "", "Такая книга уже заведена");
        }
    }
}
