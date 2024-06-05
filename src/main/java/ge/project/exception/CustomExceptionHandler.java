package ge.project.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.project.security.token.model.Token;
import ge.project.security.token.repository.TokenRepository;
import ge.project.security.user.repository.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
@AllArgsConstructor
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private final TokenRepository tokenRepository;
    private final ObjectMapper objectMapper;

    @ExceptionHandler(value = SecurityViolationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleSecurityViolation(RuntimeException ex, WebRequest request) {
        logout();
        return handleExceptionInternal(ex, null, HttpHeaders.EMPTY, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<Object> handleBusiness(RuntimeException ex, WebRequest request) {
        String body = null;
        if (ex.getMessage() != null) {
            body = objectMapper
                    .createObjectNode()
                    .put("errorCode", ex.getMessage())
                    .toString();
        }
        return handleExceptionInternal(ex, body, HttpHeaders.EMPTY, HttpStatus.CONFLICT, request);
    }

    private void logout() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        List<Token> allValidTokenByUsername = tokenRepository.findAllValidTokenByUsername(username);
        if (allValidTokenByUsername != null && !allValidTokenByUsername.isEmpty()) {
            tokenRepository.deleteAll(allValidTokenByUsername);
        }
        SecurityContextHolder.clearContext();
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> handleBadRequest(RuntimeException ex, WebRequest request) {
        String body = null;
        if (ex.getMessage() != null) {
            body = new ObjectMapper().createObjectNode().put("errorCode", ex.getMessage()).toString();
        }
        return handleExceptionInternal(ex, body, HttpHeaders.EMPTY, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleUnauthorized(RuntimeException ex, WebRequest request) {
        logout();
        return handleExceptionInternal(ex, null, HttpHeaders.EMPTY, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMultipartException(MultipartException e, WebRequest request) {
        return handleExceptionInternal(e, e.getCause().getMessage(), HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST, request);
    }
}
