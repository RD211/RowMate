package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.users.EmailInUseException;
import nl.tudelft.sem.project.users.UserNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalAPIExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({IllegalArgumentException.class,
            ConstraintViolationException.class,
            PersistenceException.class,
            MethodArgumentNotValidException.class})
    public String handleBadArgumentException(Exception ex) {
        return ex.getMessage();
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler({UserNotFoundException.class, EmailInUseException.class})
    public String handleNotFoundException(Exception ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler()
    public String handleGeneral(Exception ex) {
        return "Something went wrong. Try again. " + ex.getMessage();
    }
}

