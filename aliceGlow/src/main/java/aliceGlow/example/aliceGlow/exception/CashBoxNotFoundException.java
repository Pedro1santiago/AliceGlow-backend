package aliceGlow.example.aliceGlow.exception;

public class CashBoxNotFoundException extends RuntimeException {

    public CashBoxNotFoundException() {
        super("CashBox not found");
    }

    public CashBoxNotFoundException(Long id) {
        super("CashBox not found with id: " + id);
    }
}
