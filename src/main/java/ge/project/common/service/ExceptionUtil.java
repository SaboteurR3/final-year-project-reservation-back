package ge.project.common.service;

import ge.project.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public class ExceptionUtil {
    public static void handleDatabaseExceptions(Exception exception,
                                                Map<String, String> constraintNamesAndErrorCodes) throws ResponseStatusException {
        Throwable throwable = exception;
        do {
            String exceptionMessage = throwable.getMessage().toLowerCase();

            constraintNamesAndErrorCodes.forEach((constraintName, errorCode) -> {
                if (exceptionMessage.contains(constraintName)) {
                    throw new BusinessException(errorCode);
                }
            });

            throwable = throwable.getCause();
        } while (throwable != null);
        exception.printStackTrace();
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected PersistenceException", exception);
    }
}
