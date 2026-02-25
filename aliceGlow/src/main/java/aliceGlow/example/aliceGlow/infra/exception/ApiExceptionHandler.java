package aliceGlow.example.aliceGlow.infra.exception;

import aliceGlow.example.aliceGlow.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({
            ProductNotFoundException.class,
            SaleNotFoundException.class,
            UserNotFoundException.class
            , CashBoxNotFoundException.class
    })
    public ProblemDetail handleNotFound(RuntimeException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Not Found");
        pd.setDetail(ex.getMessage());
        enrich(pd, ex.getMessage(), request);
        return pd;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleConflict(RuntimeException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Conflict");
        pd.setDetail(ex.getMessage());
        enrich(pd, ex.getMessage(), request);
        return pd;
    }

    @ExceptionHandler(ProductInUseException.class)
    public ProblemDetail handleProductInUse(ProductInUseException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Conflict");
        pd.setDetail(ex.getMessage());
        enrich(pd, ex.getMessage(), request);
        return pd;
    }

    @ExceptionHandler(CashBoxAlreadyExistsForDateException.class)
    public ProblemDetail handleCashBoxConflict(RuntimeException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Conflict");
        pd.setDetail(ex.getMessage());
        enrich(pd, ex.getMessage(), request);
        return pd;
    }

    @ExceptionHandler({
            InsufficientStockException.class
    })
    public ProblemDetail handleBusinessConflict(RuntimeException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Business Rule Violation");
        pd.setDetail(ex.getMessage());
        enrich(pd, ex.getMessage(), request);
        return pd;
    }

    @ExceptionHandler({
            SaleWithoutItemsException.class,
            CostPriceCannotBeNegativeException.class,
            SalePriceCannotBeNegativeException.class,
            StockNegativeException.class,
            IllegalArgumentException.class
    })
    public ProblemDetail handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Bad Request");
        pd.setDetail(ex.getMessage());
        enrich(pd, ex.getMessage(), request);
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation Error");

        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        pd.setProperty("errors", errors);
        enrich(pd, "Validation Error", request);
        return pd;
    }

    private static void enrich(ProblemDetail pd, String message, HttpServletRequest request) {
        pd.setProperty("message", message);
        pd.setProperty("path", request.getRequestURI());

        String correlationId = MDC.get("correlationId");
        if (correlationId != null && !correlationId.isBlank()) {
            pd.setProperty("correlationId", correlationId);
        }
    }
}
