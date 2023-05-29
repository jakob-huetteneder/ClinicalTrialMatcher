package at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.exception.IllegalArgumentExceptionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.exception.NotFoundExceptionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.exception.ValidationExceptionDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.invoke.MethodHandles;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    protected NotFoundExceptionDto handleNotFound(NotFoundException notFoundException) {

        LOGGER.info("NotFound: {}", notFoundException.getMessage());

        return new NotFoundExceptionDto(notFoundException.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ValidationExceptionDto handleMethodArgumentNotValid(MethodArgumentNotValidException validationException) {

        LOGGER.info("ValidationException: {}", validationException.getMessage());

        List<String> errors = validationException
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .toList();

        return new ValidationExceptionDto("The input is not valid.", errors);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public IllegalArgumentExceptionDto handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {

        LOGGER.info("IllegalArgumentException: {}", illegalArgumentException.getMessage());

        return new IllegalArgumentExceptionDto(illegalArgumentException.getMessage());
    }

}
