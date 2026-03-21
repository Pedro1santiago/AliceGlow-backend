package aliceGlow.example.aliceGlow.exception;

public class SaleCannotBeEditedException extends RuntimeException {

    public SaleCannotBeEditedException() {
        super("Sale cannot be edited");
    }

    public SaleCannotBeEditedException(Long id) {
        super("Sale cannot be edited with id: " + id);
    }
}
