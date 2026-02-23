package aliceGlow.example.aliceGlow.exception;

public class InsufficientStockException extends IllegalStateException {

    public InsufficientStockException(Long productId) {
        super("Insufficient stock for productId=" + productId);
    }
}