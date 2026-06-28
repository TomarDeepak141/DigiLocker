package Spring.digiLocker.exception;

import Spring.digiLocker.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            DocumentNotFoundException.class
    )
    public ResponseEntity<ErrorResponse>
    handleDocumentNotFound(
            DocumentNotFoundException ex
    ){

        ErrorResponse response =
                new ErrorResponse();

        response.setMessage(
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }
    @ExceptionHandler(
            UserNotFoundException.class
    )
    public ResponseEntity<ErrorResponse>
    handleUserNotFound(
            UserNotFoundException ex
    ){

        ErrorResponse response =
                new ErrorResponse();

        response.setMessage(
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }
    @ExceptionHandler(
            ForbiddenException.class
    )
    public ResponseEntity<ErrorResponse>
    handleForbidden(
            ForbiddenException ex
    ){

        ErrorResponse response =
                new ErrorResponse();

        response.setMessage(
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }
    @ExceptionHandler(
            InvalidOperationException.class
    )
    public ResponseEntity<ErrorResponse>
    handleInvalidOperation(
            InvalidOperationException ex
    ){

        ErrorResponse response =
                new ErrorResponse();

        response.setMessage(
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
    @ExceptionHandler(
            AlreadySharedException.class
    )
    public ResponseEntity<ErrorResponse>
    handleAlreadyShared(
            AlreadySharedException ex
    ){

        ErrorResponse response =
                new ErrorResponse();

        response.setMessage(
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
}