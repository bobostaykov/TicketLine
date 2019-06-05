package at.ac.tuwien.sepm.groupphase.backend.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(NotFoundException ex, WebRequest request){
        String error = "Not-Found Exception: " + "Error-message = " + ex.getMessage();
        ApiError apiError = new ApiError(
            HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({PersistenceException.class})
    protected ResponseEntity<Object> handlePersitenceException (PersistenceException ex, WebRequest request){
        String error = "Persistence Exception: Error = " + ex.getMessage();
        ApiError apiError = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR,ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    @ExceptionHandler({ServiceException.class})
    private ResponseEntity<Object> handleServiceException(ServiceException ex, WebRequest request){
        String error = "Service Exception: Error = " + ex.getMessage();
        ApiError apiError = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR,ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    @ExceptionHandler({AccessDeniedException.class})
    private ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request){
        String error = "Access denied: Error = " + ex.getMessage();
        ApiError apiError = new ApiError(
            HttpStatus.FORBIDDEN,ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    @ExceptionHandler({ValidationException.class})
    private ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request){
        String error = "Access denied: Error = " + ex.getMessage();
        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST,ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    /*

    @ExceptionHandler({MissingServletRequestParameterException.class})
    private ResponseEntity<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, WebRequest request){
        String error = "Bad Request: Error = " + ex.getMessage();
        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST,ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    // default implementation
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> defaultExceptionHandler(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Error occured");
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
   */

}
