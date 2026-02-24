package aliceGlow.example.aliceGlow.exception;

public class ProductInUseException extends RuntimeException {

    public ProductInUseException(Long productId) {
        super("Product cannot be deleted because it is referenced by sales (sale_items). Product id: " + productId);
    }
}
