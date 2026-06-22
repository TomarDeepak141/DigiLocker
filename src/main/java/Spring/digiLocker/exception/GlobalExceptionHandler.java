package Spring.digiLocker.exception;

import Spring.digiLocker.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            RuntimeException.class
    )
    public ResponseEntity<ErrorResponse>
    handleRuntimeException(
            RuntimeException ex
    ) {

        ErrorResponse response =
                new ErrorResponse();

        response.setMessage(
                ex.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }
}