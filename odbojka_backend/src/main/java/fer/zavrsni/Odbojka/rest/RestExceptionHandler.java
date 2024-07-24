package fer.zavrsni.Odbojka.rest;

import fer.zavrsni.Odbojka.service.RequestDeniedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<?> handleIllegalArgument(Exception e) {
        Map<String, String> props = new HashMap<>();
        props.put("message", e.getMessage());
        props.put("status", "400");
        props.put("error", "Bad Request");
        return new ResponseEntity<>(props, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RequestDeniedException.class)
    protected ResponseEntity<?> handleRequestDenied(RequestDeniedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("status", "400");
        response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

