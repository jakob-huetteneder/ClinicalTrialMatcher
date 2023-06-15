package at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.exception.MessageExceptionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.exception.ValidationExceptionDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.AlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * This class handles exceptions thrown by the controllers.
 * It returns a response with the appropriate status code and message.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    protected MessageExceptionDto handleNotFound(NotFoundException notFoundException) {
        LOG.trace("handleNotFound({})", notFoundException.getMessage());

        return new MessageExceptionDto(notFoundException.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ValidationExceptionDto handleMethodArgumentNotValid(MethodArgumentNotValidException validationException) {
        LOG.trace("handleMethodArgumentNotValid({})", validationException.getMessage());

        List<String> errors = validationException
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .toList();

        return new ValidationExceptionDto("The input is not valid.", errors);
    }

    @ExceptionHandler(value = {AlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public MessageExceptionDto handleAlreadyExistsException(AlreadyExistsException alreadyExistsException) {
        LOG.trace("handleAlreadyExistsException({})", alreadyExistsException.getMessage());

        return new MessageExceptionDto(alreadyExistsException.getMessage());
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MessageExceptionDto handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        LOG.trace("handleIllegalArgumentException({})", illegalArgumentException.getMessage());

        return new MessageExceptionDto(illegalArgumentException.getMessage());
    }

    @ExceptionHandler(value = {BadCredentialsException.class, UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public MessageExceptionDto handleBadCredentialsException(Exception authenticationException) {
        LOG.trace("handleBadCredentialsException({})", authenticationException.getMessage());

        return new MessageExceptionDto(authenticationException.getMessage());
    }
}
