package at.ac.tuwien.sepm.groupphase.backend.exception;

import com.itextpdf.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomRestExceptionHandler.class);

    @ExceptionHandler({NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(NotFoundException ex, WebRequest request){
        String error = "Not-Found Exception: " + "Error-message = " + ex.getMessage();
        ApiError apiError = new ApiError(
            HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
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
    private ResponseEntity<Object> handleAccessDeniedException(org.springframework.security.access.AccessDeniedException ex, WebRequest request){
        String error = "Access denied: Error = " + ex.getMessage();
        ApiError apiError = new ApiError(
            HttpStatus.FORBIDDEN,ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    @ExceptionHandler({CustomValidationException.class})
    private ResponseEntity<Object> handleValidationException(CustomValidationException ex, WebRequest request){
        String error = "Validation Error: Error = " + ex.getMessage();
        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST,ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    @ExceptionHandler({DocumentException.class})
    private ResponseEntity<Object> handleDocumentException(DocumentException ex, WebRequest request){
        String message = "Internal Server Error: Could not create receipt PDF";
        LOGGER.info("DocumentException: " + ex.getMessage());
        ApiError apiError = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR, message, ex.getLocalizedMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    @ExceptionHandler({IOException.class})
    private ResponseEntity<Object> handleIOException(IOException ex, WebRequest request){
        String message = "Internal Server Error: Could not create receipt PDF";
        LOGGER.info("IOException: " + ex.getMessage());
        ApiError apiError = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR, message, ex.getLocalizedMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    // MethodArgumentNotValidException gets thrown by JavaX Validation of request bodies. Override to get error messages
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiError apiError =
            new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(
            ex, apiError, headers, apiError.getStatus(), request);
    }
    // Validation throws ConstraintViolationException if not applied on request body, but we still want a HTTP Bad_REQUEST here
    @ExceptionHandler({ConstraintViolationException.class})
    private ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request){
        String error = "Validation Error: Error = " + ex.getMessage();
        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST,ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    //to catch custom handled exceptions
    @ExceptionHandler({ResponseStatusException.class})
    private ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex, WebRequest request){
        String error = ex.getMessage();
        ApiError apiError = new ApiError(
            ex.getStatus(),ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    private ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request){
        String error = "Data Integrity Violation: Error = " + ex.getMessage();
        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST,ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> defaultExceptionHandler(Exception ex, WebRequest request) {
        LOGGER.info(ex.getClass().getCanonicalName() + "is thrown!");
        ApiError apiError = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Error occured");
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }



}
