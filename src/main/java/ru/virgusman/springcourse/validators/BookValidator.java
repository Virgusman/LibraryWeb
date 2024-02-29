package ru.virgusman.springcourse.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.virgusman.springcourse.dao.BookDAO;
import ru.virgusman.springcourse.models.Book;

@Component
public class BookValidator implements Validator {
    private BookDAO bookDAO;

    public BookValidator(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    //Поиск идентичной книги в БД
    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;
        if (bookDAO.countBooks(book) > 0){
            errors.rejectValue("name", "", "Такая книга уже заведена");
        }
    }
}
