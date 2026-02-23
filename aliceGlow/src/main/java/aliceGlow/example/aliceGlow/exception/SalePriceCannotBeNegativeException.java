package aliceGlow.example.aliceGlow.exception;

public class SalePriceCannotBeNegativeException extends IllegalArgumentException{

    public SalePriceCannotBeNegativeException(){
        super("Sale Price cannot be negative");
    }
}